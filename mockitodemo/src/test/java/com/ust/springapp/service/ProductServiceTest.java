package com.ust.springapp.service;

import com.ust.springapp.exceptions.ProductNotFoundException;
import com.ust.springapp.model.Product;
import com.ust.springapp.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addProduct() {
        Product product = new Product("Dell XPS Laptop",250000,25);
        when(productRepository.save(product)).thenReturn(new Product(1,"Dell XPS Laptop",250000,25));

        Product responseProduct = productService.addProduct(product);

        assertNotNull(responseProduct);
        assertEquals(1,responseProduct.getProductId());
        assertEquals("Dell XPS Laptop",responseProduct.getName());
        assertEquals(250000,responseProduct.getPrice());
        assertEquals(25,responseProduct.getStock());
    }


    @Test
    void getProductByIdSuccess() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(new Product(1,"Dell XPS Laptop",250000,25)));

        Product product = productService.getProductById(1);

        assertNotNull(product);
        assertEquals(1L,product.getProductId());
        assertEquals("Dell XPS Laptop",product.getName());
        assertEquals(250000,product.getPrice());
        assertEquals(25,product.getStock());
    }

    @Test
    void getProductByIdFail() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class,()->{
            productService.getProductById(1L);
        });

    }
}