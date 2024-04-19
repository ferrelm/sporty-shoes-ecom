package com.controller;

import com.entity.Product;
import com.service.LoginService;
import com.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.ui.Model;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {
    @Mock
    private ProductService productService;

    @Mock
    private LoginService loginService;

    @Mock
    private Model model;

    @InjectMocks
    private AdminController adminController;

    @Test
    void testOpen() {
        when(productService.findAllProducts()).thenReturn(Collections.emptyList());
        when(productService.orderDetails()).thenReturn(Collections.emptyList());
        when(loginService.findAllUsers()).thenReturn(Collections.emptyList());

        String view = adminController.open(model, new Product());
        assertEquals("admin", view);
        verify(productService).findAllProducts();
        verify(productService).orderDetails();
        verify(loginService).findAllUsers();
    }

    @Test
    void testAddProductDetails() {
        Product product = new Product();
        product.setPid(1);
        product.setPname("Sporty Shoe");
        product.setPrice(100.0f);
        product.setCategory("Sports");

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getParameter("b1")).thenReturn("Store Product");
        when(productService.storeProduct(any())).thenReturn("Product stored successfully");

        String view = adminController.addProductDetails(model, product, mockRequest);
        assertEquals("admin", view);
        verify(productService).storeProduct(product);
        verify(model).addAttribute(eq("msg"), anyString());
    }

    @Test
    void testDeleteProductById() {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getParameter("pid")).thenReturn("1");
        when(productService.deleteProduct(1)).thenReturn("Product deleted successfully");

        String view = adminController.deleteProductById(model, new Product(), mockRequest);
        assertEquals("admin", view);
        verify(productService).deleteProduct(1);
        verify(model).addAttribute("msg", "Product deleted successfully");
    }

}

