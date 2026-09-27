package fr._42.orm.main;

import fr._42.orm.manager.OrmManager;
import fr._42.orm.models.Product;
import fr._42.orm.models.User;

import java.sql.Connection;
import java.sql.DriverManager;

public class Main {

    public static void main(String[] args)
            throws Exception {

        String url =
            "jdbc:postgresql://localhost:4321/orm";

        String username = "postgres";
        String password = "postgres";

        try (Connection connection =
                DriverManager.getConnection(
                    url,
                    username,
                    password
                )) {

            OrmManager orm =
                new OrmManager(
                    connection,
                    User.class,
                    Product.class
                );
        }
    }
}
