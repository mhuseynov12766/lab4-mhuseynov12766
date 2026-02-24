package az.edu.ada.wm2.lab4.service;

import az.edu.ada.wm2.lab4.model.Product;
import az.edu.ada.wm2.lab4.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProductServiceImplTest {

    @Autowired
    private ProductService productService;

    @MockitoBean
    private ProductRepository productRepository;

    @Test
    void createProduct_shouldCallRepositorySave() {
        Product product = new Product(
                "Juice",
                new BigDecimal("3.50"),
                LocalDate.now().plusDays(3));

        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Product created = productService.createProduct(product);

        verify(productRepository, times(1)).save(any(Product.class));
        assertNotNull(created.getId());
    }

    @Test
    void getProductById_shouldReturnProduct() {
        UUID id = UUID.randomUUID();
        Product product = new Product(id, "Tea",
                new BigDecimal("2.50"),
                LocalDate.now().plusDays(7));

        when(productRepository.findById(id))
                .thenReturn(Optional.of(product));

        Product found = productService.getProductById(id);

        assertEquals("Tea", found.getProductName());
    }

    @Test
    void getProductById_shouldThrowException_whenNotFound() {
        UUID id = UUID.randomUUID();

        when(productRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> productService.getProductById(id));
    }

    @Test
    void getProductsByPriceRange_shouldFilterCorrectly() {
        Product p1 = new Product("A", new BigDecimal("10"), LocalDate.now());
        Product p2 = new Product("B", new BigDecimal("50"), LocalDate.now());
        Product p3 = new Product("C", new BigDecimal("150"), LocalDate.now());

        when(productRepository.findAll())
                .thenReturn(List.of(p1, p2, p3));

        List<Product> result = productService.getProductsByPriceRange(
                new BigDecimal("5"),
                new BigDecimal("100"));

        assertEquals(2, result.size());
    }
}