package fr._42.reflection.main;

import fr._42.reflection.classes.Car;
import fr._42.reflection.classes.User;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Class<?>[] classes = { User.class, Car.class };

        System.out.println("Classes:");
        for (Class<?> clazz : classes) {
            System.out.println(clazz.getSimpleName());
        }

        System.out.println("---------------------");
        Class<?> selectedClass = getClassByName(classes);
        if (selectedClass == null) {
            System.out.println("Class not found. Please try again.");
            System.exit(1);
        }

        System.out.println("---------------------");
        printClassInfo(selectedClass);
        
        System.out.println("---------------------");
        Object obj = createObj(selectedClass);
        System.out.println("Object created: " + obj.toString());

        System.out.println("---------------------");
        updateObject(obj);
        System.out.println("Object updated: " + obj.toString());

        System.out.println("---------------------");
        callMethod(obj);
    
        scanner.close();
    }

    private static Class<?> getClassByName(Class<?>[] classes) {
        String className = null;

        try {
            System.out.println("Enter class name:");
            className = scanner.nextLine().trim();

            Class<?> selectedClass = null;
            for (Class<?> clazz : classes) {
                if (clazz.getSimpleName().equals(className)) {
                    selectedClass = clazz;
                    return selectedClass;
                }
            }

            throw new ClassNotFoundException();
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + className);
            System.exit(1);
            return null;
        }
    }

    private static void printClassInfo(Class<?> clazz) {
        try {
            System.out.println("Fields:");
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                System.out.println("   " + field.getType().getSimpleName() + " " + field.getName());
            }

            System.out.println("Methods:");
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals("toString") && method.getParameterCount() == 0) {
                    continue;
                }

                System.out.print("   " + method.getReturnType().getSimpleName() + " " + method.getName() + "(");
                Class<?>[] parameterTypes = method.getParameterTypes();
                for (int i = 0; i < parameterTypes.length; i++) {
                    System.out.print(parameterTypes[i].getSimpleName());

                    if (i < parameterTypes.length - 1) {
                        System.out.print(", ");
                    }
                }

                System.out.println(")");
            }
        } catch (Exception e) {
            System.out.println("Error retrieving class information: " + e.getMessage());
            System.exit(1);
        }
    }

    private static Object createObj(Class<?> clazz) {
        try {
            System.out.println("Let's create an object.");
            Constructor<?> constructor = null;
            for (Constructor<?> current : clazz.getDeclaredConstructors()) {
                if (constructor == null || current.getParameterCount() > constructor.getParameterCount()) {
                    constructor = current;
                }
            }

            Parameter[] parameters = constructor.getParameters();
            Object[] args = new Object[parameters.length];

            for (int i = 0; i < parameters.length; i++) {
                System.out.println(parameters[i].getName() + ":");
                String input = scanner.nextLine().trim();

                args[i] = parseValue(
                    input,
                    parameters[i].getType()
                );
            }

            return constructor.newInstance(args);
        } catch (Exception e) {
            System.out.println("Error creating object: " + e.getMessage());
            System.exit(1);
            return null;
        }
    }

    private static Object parseValue(String value,Class<?> type) {
        if (type == String.class) { return value;}
        if (type == Integer.class || type == int.class) { return Integer.parseInt(value);}
        if (type == Double.class || type == double.class) { return Double.parseDouble(value);}
        if (type == Boolean.class || type == boolean.class) { return Boolean.parseBoolean(value);}
        if (type == Long.class || type == long.class) { return Long.parseLong(value);}

        throw new IllegalArgumentException("Unsupported type: " + type);
    }

    private static void updateObject(Object obj) {
        try {
            Class<?> clazz = obj.getClass();
            System.out.println("Enter name of the field for changing:");
            String fieldName = scanner.nextLine().trim();

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().equals(fieldName)) {
                    field.setAccessible(true);
                    System.out.println("Enter String value::");
                    String input = scanner.nextLine().trim();
                    
                    Object newValue = parseValue(input, field.getType());
                    field.set(obj, newValue);

                    return;    
                }
            }

            throw new NoSuchFieldException("Field not found: " + fieldName);
        } catch (Exception e) {
            System.out.println("Error updating object: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void callMethod(Object obj) {
        try {
            Class<?> clazz = obj.getClass();
            System.out.println("Enter name of the method for call:");
            String methodName = scanner.nextLine().trim();

            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                Parameter[] parameters = method.getParameters();
                Object[] args = new Object[parameters.length];
                String expectedMethod =
                            method.getName()
                            + "("
                            + Arrays.stream(method.getParameterTypes())
                                .map(Class::getSimpleName)
                                .collect(Collectors.joining(","))
                            + ")";

                if (expectedMethod.equals(methodName)) {
                    for (int i = 0; i < parameters.length; i++) {
                        System.out.println("Enter " + parameters[i].getType().getSimpleName() + " value:");
                        String input = scanner.nextLine().trim();
                        args[i] = parseValue(input, parameters[i].getType());
                    }

                    Object result = method.invoke(obj, args);
                    if (method.getReturnType() != void.class) {
                        System.out.println("Method returned:\n" + result);
                    }
                    return;
                }
            }

            throw new NoSuchMethodException("Method not found: " + methodName);
        } catch (Exception e) {
            System.out.println("Error calling method: " + e.getMessage());
            System.exit(1);
        }
    }
}
