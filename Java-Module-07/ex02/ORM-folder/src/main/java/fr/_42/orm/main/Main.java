package fr._42.orm.main;

import fr._42.orm.manager.OrmManager;
import fr._42.orm.models.Product;
import fr._42.orm.models.User;

import java.sql.Connection;
import java.sql.DriverManager;

public class Main {

    public static void main(String[] args) throws Exception {

        String url = "jdbc:postgresql://localhost:4321/orm";
        String username = "postgres";
        String password = "postgres";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            OrmManager orm = new OrmManager(
                    connection,
                    User.class,
                    Product.class
            );

            System.out.println("\n=== SAVE USER ===");
            User user = new User("Khalil", 21);
            System.out.println("Before save: " + user);

            orm.save(user);
            System.out.println("After save:  " + user);


            System.out.println("\n=== FIND USER ===");
            User foundUser = orm.findById(user.getId(), User.class);
            System.out.println("Found: " + foundUser);


            System.out.println("\n=== UPDATE USER ===");
            foundUser.setFirstName("KSohail");
            foundUser.setAge(22);

            orm.update(foundUser);
            User updatedUser = orm.findById(foundUser.getId(), User.class);
            System.out.println("Updated: " + updatedUser);


            System.out.println("\n=== UPDATE USER WITH NULL ===");
            updatedUser.setFirstName(null);

            orm.update(updatedUser);
            User nullUser = orm.findById(updatedUser.getId(), User.class);
            System.out.println("After null update: " + nullUser);
            System.out.println(nullUser.getFirstName() == null);

            System.out.println("\n=== SAVE PRODUCT ===");
            Product product = new Product("ThinkPad P1", 17999.99);

            System.out.println("Before save: " + product);
            orm.save(product);
            System.out.println("After save:  " + product);


            System.out.println("\n=== FIND PRODUCT ===");
            Product foundProduct = orm.findById(product.getId(), Product.class);
            System.out.println("Found: " + foundProduct);


            System.out.println("\n=== UPDATE PRODUCT ===");
            foundProduct.setProductName("ThinkPad P1 Gen 6");
            foundProduct.setPrice(2199.99);

            orm.update(foundProduct);
            Product updatedProduct = orm.findById(foundProduct.getId(), Product.class);
            System.out.println("Updated: " + updatedProduct);
        }
    }
}