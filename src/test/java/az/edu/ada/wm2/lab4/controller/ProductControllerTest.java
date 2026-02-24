package az.edu.ada.wm2.lab4.controller;

import az.edu.ada.wm2.lab4.model.Product;
import az.edu.ada.wm2.lab4.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private tools.jackson.databind.ObjectMapper objectMapper;

    @Test
    void createProduct_shouldReturn201() throws Exception {
        Product product = new Product(
                UUID.randomUUID(),
                "Chocolate",
                new BigDecimal("8.00"),
                LocalDate.now().plusDays(20));

        when(productService.createProduct(any(Product.class)))
                .thenReturn(product);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productName").value("Chocolate"));
    }

    @Test
    void getProductById_shouldReturn200() throws Exception {
        UUID id = UUID.randomUUID();
        Product product = new Product(
                id,
                "Coffee",
                new BigDecimal("12.00"),
                LocalDate.now().plusDays(15));

        when(productService.getProductById(id))
                .thenReturn(product);

        mockMvc.perform(get("/api/products/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Coffee"));
    }

    @Test
    void getProductById_shouldReturn404_whenNotFound() throws Exception {
        UUID id = UUID.randomUUID();

        when(productService.getProductById(id))
                .thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/products/{id}", id))
                .andExpect(status().isNotFound());
    }
}