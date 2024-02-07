package edu.school21.repositories;


import edu.school21.models.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ProductsRepositoryJdbcImplTest {
    private static final String CORRECT_DB_NAME = "correctDb";
    private static final String INCORRECT_DB_NAME = "incorrectDb";

    private static final String DB_SCHEMA = "schema.sql";
    private static final String DB_DATA = "data.sql";

    private EmbeddedDatabase correctTestDb;
    private EmbeddedDatabase noCorrectTestDb;
    private ProductsRepositoryJdbcImpl correctProductsRepository;
    private ProductsRepositoryJdbcImpl noCorrectProductsRepository;

    final List<Product> TEST_PRODUCTS = Arrays.asList(
            new Product(0, "unique_product_1", 50),
            new Product(1, "unique_product_2", 100),
            new Product(2, "unique_product_3", 500),
            new Product(3, "unique_product_4", 5000),
            new Product(4, "unique_product_5", 10000)
    );

    @BeforeEach
    public void init() {
        correctTestDb = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .setName(CORRECT_DB_NAME)
                .addScript(DB_SCHEMA)
                .addScript(DB_DATA)
                .build();
        noCorrectTestDb = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .setName("TestDB")
                .build();

        correctProductsRepository = new ProductsRepositoryJdbcImpl(correctTestDb);
        noCorrectProductsRepository = new ProductsRepositoryJdbcImpl(noCorrectTestDb);
    }

    @AfterEach
    public void destroy() {
        correctTestDb.shutdown();
        noCorrectTestDb.shutdown();
    }

    @Test
    public void testFindAll() {
        List<Product> products = correctProductsRepository.findAll();
        assertEquals(TEST_PRODUCTS, products,
                "Found products do not match expected");
        assertFalse(products.isEmpty(), "Found products list should not be empty");
        assertThrows(RuntimeException.class, () -> {
            noCorrectProductsRepository.findAll();
        });
    }

    @Test
    public void testFindById() {

        long existingProductId = 4L;
        long nonExistentProductId = 123L;
        Optional<Product> productOptional = correctProductsRepository.findById(existingProductId);
        assertTrue(productOptional.isPresent(), "Existing product not found by ID");
        Product foundProduct = productOptional.get();
        Product expectedProduct = TEST_PRODUCTS.get(4);
        assertEquals(expectedProduct, foundProduct, "Found product does not match expected");
        assertFalse(correctProductsRepository.findById(nonExistentProductId).isPresent(),
                "Non-existent product should not be found");
        correctTestDb.shutdown();

        assertThrows(RuntimeException.class,
                () -> correctProductsRepository.findById(existingProductId));

    }

    @Test
    public void testUpdate() {
        long existingProductId = 4L;
        Product updatedProduct = new Product(existingProductId, "Updated Name", 999);
        long nonExistentProductId = 123L;
        Product invalidProduct = new Product(nonExistentProductId, "Invalid Name", 999);
        correctProductsRepository.update(updatedProduct);
        Optional<Product> productOptional = correctProductsRepository.findById(existingProductId);
        assertTrue(productOptional.isPresent());
        Product product = productOptional.get();
        assertEquals(existingProductId, product.getId());
        assertEquals("Updated Name", product.getName());
        assertEquals(999, product.getPrice());
        try {
            noCorrectProductsRepository.update(invalidProduct);
            fail("Expected RuntimeException when updating nonexistent product");
        } catch (RuntimeException e) {
            assertTrue(true);
        }

    }

    @Test
    public void testSave() {

        // Подготовка данных
        long productId = 5L;
        String productName = "product_test_6";
        int productPrice = 5600;
        Product product = new Product(productId, productName, productPrice);
        correctProductsRepository.save(product);
        Optional<Product> savedProductOptional = correctProductsRepository.findById(productId);
        assertTrue(savedProductOptional.isPresent(), "Saved product not found by id");
        Product savedProduct = savedProductOptional.get();
        assertEquals(productId, savedProduct.getId(), "Saved product has wrong id");
        assertEquals(productName, savedProduct.getName(), "Saved product has wrong name");
        assertEquals(productPrice, savedProduct.getPrice(), "Saved product has wrong price");
        try {
            noCorrectTestDb.shutdown();
            noCorrectProductsRepository.save(product);
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            assertTrue(true);
        }

    }

    @Test
    public void testDelete() {
        long existingProductId = 4L;
        long nonExistentProductId = 2222L;
        Optional<Product> productOptional = correctProductsRepository.findById(existingProductId);
        assertTrue(productOptional.isPresent(), "Existing product not found before delete");
        correctProductsRepository.delete(existingProductId);
        productOptional = correctProductsRepository.findById(existingProductId);
        assertFalse(productOptional.isPresent(), "Existing product was not deleted");

        try {
            noCorrectTestDb.shutdown();
            noCorrectProductsRepository.delete(nonExistentProductId);
            fail("Expected RuntimeException on deleting from inactive DB");
        } catch (RuntimeException e) {
            assertTrue(true);
        }
    }

}
