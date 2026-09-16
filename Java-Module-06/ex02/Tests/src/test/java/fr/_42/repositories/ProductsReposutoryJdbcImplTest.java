package fr._42.repositories;

import fr._42.models.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ProductsReposutoryJdbcImplTest {

    private DataSource dataSource;
    private ProductsRepository repository;

    private final List<Product> EXPECTED_FIND_ALL_PRODUCTS = List.of(
        new Product(1L, 1000.0, "Laptop"),
        new Product(2L, 40.0, "Mouse"),
        new Product(3L, 80.0, "Keyboard"),
        new Product(4L, 300.0, "Monitor"),
        new Product(5L, 100.0, "Headphones")
    );

    private final Product EXPECTED_FIND_BY_ID_PRODUCT = new Product(2L, 40.0, "Mouse");
    private final Product EXPECTED_UPDATED_PRODUCT = new Product(2L, 75.0, "Gaming Mouse");

    @BeforeEach
    void init() {
        dataSource = new EmbeddedDatabaseBuilder()
            .generateUniqueName(true)
            .setType(EmbeddedDatabaseType.HSQL)
            .addScript("schema.sql")
            .addScript("data.sql")
            .build();

        repository = new ProductsReposutoryJdbcImpl(dataSource);
    }

    @Test
    void findAllReturnsAllProducts() {
        List<Product> products = repository.findAll();
        assertEquals(EXPECTED_FIND_ALL_PRODUCTS, products);
    }

    @Test
    void findByIdReturnsCorrectProduct() {
        Optional<Product> product = repository.findById(2L);

        assertTrue(product.isPresent());
        assertEquals(EXPECTED_FIND_BY_ID_PRODUCT, product.get());
    }

    @Test
    void findByIdReturnsEmptyWhenProductDoesNotExist() {
        Optional<Product> product = repository.findById(999L);
        assertTrue(product.isEmpty());
    }

    @Test
    void updateChangesProduct() {
        repository.update(EXPECTED_UPDATED_PRODUCT);
        Optional<Product> product = repository.findById(2L);

        assertTrue(product.isPresent());
        assertEquals(EXPECTED_UPDATED_PRODUCT, product.get());
    }

    @Test
    void saveAddsProduct() {
        Product newProduct = new Product(
            null,
            90.0,
            "Webcam"
        );

        repository.save(newProduct);
        assertNotNull(newProduct.getIdentifier());
        Optional<Product> product = repository.findById(newProduct.getIdentifier());

        assertTrue(product.isPresent());
        assertEquals(newProduct, product.get());
    }

    @Test
    void deleteRemovesProduct() {
        repository.delete(2L);
        Optional<Product> product = repository.findById(2L);

        assertTrue(product.isEmpty());
    }
}