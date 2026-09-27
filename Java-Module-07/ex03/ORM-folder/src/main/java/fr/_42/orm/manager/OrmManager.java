package fr._42.orm.manager;

import fr._42.orm.annotations.OrmColumnId;
import fr._42.orm.annotations.OrmColumn;
import fr._42.orm.annotations.OrmEntity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;

public class OrmManager {
    private final Connection connection;

    public OrmManager(Connection connection, Class<?>... entityClasses) {
        this.connection = connection;

        for (Class<?> clazz : entityClasses) {
            if (!clazz.isAnnotationPresent(OrmEntity.class)) {
                continue;
            }

            initializeEntity(clazz);
        }
    }

    private void initializeEntity(Class<?> clazz) {
        try {
            OrmEntity entity = clazz.getAnnotation(OrmEntity.class);
            String tableName = entity.table();

            String dropSql = "DROP TABLE IF EXISTS " + tableName;
            System.out.println(dropSql);
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(dropSql);
            }

            String createSql = buildCreateTableSql(clazz);
            System.out.println(createSql);
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(createSql);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize entity: " + clazz.getName(), e);
        }
    }

    private String getSqlType(Class<?> javaType, int length) {
        switch (javaType.getSimpleName()) {
            case "String":
                return "VARCHAR(" + length + ")";
            case "Integer":
                return "INTEGER";
            case "Long":
                return "BIGINT";
            case "Double":
                return "DOUBLE PRECISION";
            case "Boolean":
                return "BOOLEAN";
            default:
                throw new IllegalArgumentException(
                    "Unsupported Java type: " + javaType.getName()
                );
        }
    }

    private String buildCreateTableSql(Class<?> clazz) {
        OrmEntity entity = requireEntity(clazz);
        List<String> columns = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(OrmColumnId.class)) {
                columns.add(field.getName() + " BIGSERIAL PRIMARY KEY");
                continue;
            }

            OrmColumn column = field.getAnnotation(OrmColumn.class);
            if (column == null) {
                continue;
            }
            columns.add(
                    column.name()
                    + " "
                    + getSqlType(
                        field.getType(),
                        column.length()
                    )
            );
        }

        return "CREATE TABLE "
                + entity.table()
                + " ("
                + String.join(", ", columns)
                + ")";
    }

    public void save(Object entity) {
        try {
            Class<?> clazz = entity.getClass();

            OrmEntity ormEntity = requireEntity(clazz);
            Field idField = getIdField(clazz);
            List<Field> columns = getColumnFields(clazz);

            String columnNames = columns.stream()
                    .map(field -> field.getAnnotation(OrmColumn.class).name())
                    .collect(Collectors.joining(", "));

            String placeholders = columns.stream()
                    .map(field -> "?")
                    .collect(Collectors.joining(", "));

            String sql = "INSERT INTO "
                    + ormEntity.table()
                    + " ("
                    + columnNames
                    + ") VALUES ("
                    + placeholders
                    + ")";

            System.out.println(sql);
            try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < columns.size(); i++) {
                    Field field = columns.get(i);
                    field.setAccessible(true);
                    statement.setObject(i + 1, field.get(entity));
                }

                statement.executeUpdate();
                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (keys.next()) {
                        idField.setAccessible(true);
                        idField.set(entity, keys.getLong(1));
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to save entity", e);
        }
    }

    public void update(Object entity) {
        try {
            Class<?> clazz = entity.getClass();
            OrmEntity ormEntity = requireEntity(clazz);
            Field idField = getIdField(clazz);

            idField.setAccessible(true);

            Object idValue = idField.get(entity);
            if (idValue == null) {
                throw new IllegalArgumentException("Cannot update entity with null id");
            }

            List<Field> columns = getColumnFields(clazz);
            String assignments = columns.stream()
                    .map(field -> field.getAnnotation(OrmColumn.class).name() + " = ?")
                    .collect(Collectors.joining(", "));

            String sql = "UPDATE "
                    + ormEntity.table()
                    + " SET "
                    + assignments
                    + " WHERE "
                    + idField.getName()
                    + " = ?";

            System.out.println(sql);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                int index = 1;

                for (Field field : columns) {
                    field.setAccessible(true);
                    statement.setObject(index++, field.get(entity));
                }

                statement.setObject(index, idValue);
                statement.executeUpdate();
            }

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to update entity",
                    e
            );
        }
    }

    public <T> T findById(Long id, Class<T> aClass) {
        try {
            OrmEntity entity = requireEntity(aClass);
            Field idField = getIdField(aClass);
            String sql = "SELECT * FROM "
                    + entity.table()
                    + " WHERE "
                    + idField.getName()
                    + " = ?";

            System.out.println(sql);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.next()) {
                        return null;
                    }

                    Constructor<T> constructor = aClass.getDeclaredConstructor();

                    constructor.setAccessible(true);
                    
                    T object = constructor.newInstance();

                    idField.setAccessible(true);
                    idField.set(object, resultSet.getObject(idField.getName()));
                    for (Field field : getColumnFields(aClass)) {
                        field.setAccessible(true);

                        OrmColumn column = field.getAnnotation(OrmColumn.class);
                        Object value = resultSet.getObject(column.name());
                        field.set(object, value);
                    }
                    return object;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to find entity", e);
        }
    }

    private OrmEntity requireEntity(Class<?> clazz) {
        OrmEntity entity = clazz.getAnnotation(OrmEntity.class);

        if (entity == null) {
            throw new IllegalArgumentException(clazz.getName() + " is not annotated with @OrmEntity");
        }

        return entity;
    }

    private Field getIdField(Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(OrmColumnId.class)) {
                return field;
            }
        }

        throw new IllegalArgumentException("No @OrmColumnId field found in " + clazz.getName());
    }

    private List<Field> getColumnFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(OrmColumn.class)) {
                fields.add(field);
            }
        }

        return fields;
    }
}