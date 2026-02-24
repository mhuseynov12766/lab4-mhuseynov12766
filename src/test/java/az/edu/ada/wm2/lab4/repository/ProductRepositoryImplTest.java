package az.edu.ada.wm2.lab4.repository;

import az.edu.ada.wm2.lab4.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductRepositoryImplTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void saveAndFindById_shouldWork() {
        Product product = new Product(
                "Milk",
                new BigDecimal("5.50"),
                LocalDate.now().plusDays(5));

        Product saved = productRepository.save(product);

        Optional<Product> found = productRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("Milk", found.get().getProductName());
    }

    @Test
    void existsById_shouldReturnTrueAfterSave() {
        Product product = new Product(
                "Bread",
                new BigDecimal("2.00"),
                LocalDate.now().plusDays(2));

        Product saved = productRepository.save(product);

        assertTrue(productRepository.existsById(saved.getId()));
    }

    @Test
    void deleteById_shouldRemoveProduct() {
        Product product = new Product(
                "Eggs",
                new BigDecimal("4.00"),
                LocalDate.now().plusDays(10));

        Product saved = productRepository.save(product);

        productRepository.deleteById(saved.getId());

        assertFalse(productRepository.existsById(saved.getId()));
    }
}