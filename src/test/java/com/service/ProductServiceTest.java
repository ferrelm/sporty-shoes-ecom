package com.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.entity.Product;
import com.repository.ProductRepository;

@SpringBootTest
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void testStoreProduct() {
        Product product = new Product();
        product.setPid(1);
        product.setPname("Sporty Shoe");
        product.setPrice(100.0f);
        product.setQuantity(50);

        when(productRepository.save(any(Product.class))).thenReturn(product);

        String result = productService.storeProduct(product);
        assertEquals("Product record stored successfully", result);
        verify(productRepository, times(1)).save(product);
    }
}

