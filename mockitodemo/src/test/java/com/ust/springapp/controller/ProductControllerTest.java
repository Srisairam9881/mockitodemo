package com.ust.springapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ust.springapp.exceptions.ProductNotFoundException;
import com.ust.springapp.model.Product;
import com.ust.springapp.service.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    //Method converts Product Object to JSON string
    public static String asJsonString(Product product){
        try {
            return new ObjectMapper().writeValueAsString(product);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addProduct() {
        Product requestProduct = new Product("Dell XPS Laptop",250000,25);
        when(productService.addProduct(requestProduct)).thenReturn(new Product(1,"Dell XPS Laptop",250000,25));

        try {
            MvcResult mvcResult =  mockMvc.perform(post("/api/product")
                                                    .contentType(MediaType.APPLICATION_JSON)
                                                    .content(asJsonString(requestProduct))).andDo(print()).andReturn();

            assertEquals(201,mvcResult.getResponse().getStatus());

            String jsonProduct = mvcResult.getResponse().getContentAsString();
            Product responseProduct = new ObjectMapper().readValue(jsonProduct,Product.class);

            assertEquals(1L,responseProduct.getProductId());
            assertEquals("Dell XPS Laptop",responseProduct.getName());
            assertEquals(250000,responseProduct.getPrice());
            assertEquals(25,responseProduct.getStock());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getProductSuccess() {
        Product product = new Product(1l,"Dell XPS Laptop",250000,25);
        when(productService.getProductById(1)).thenReturn(new Product(1,"Dell XPS Laptop",250000,25));

        try {
            MvcResult mvcResult =  mockMvc.perform(get("/api/product/1")
                                                .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andReturn();

            assertEquals(200,mvcResult.getResponse().getStatus());

            String jsonProduct = mvcResult.getResponse().getContentAsString();
            Product responseProduct = new ObjectMapper().readValue(jsonProduct,Product.class);

            assertEquals(1L,responseProduct.getProductId());
            assertEquals("Dell XPS Laptop",responseProduct.getName());
            assertEquals(250000,responseProduct.getPrice());
            assertEquals(25,responseProduct.getStock());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getProductNotFound() {
        Product product = new Product(1l,"Dell XPS Laptop",250000,25);
        when(productService.getProductById(1)).thenThrow(new ProductNotFoundException());

        try {
            MvcResult mvcResult =  mockMvc.perform(get("/api/product/1")
                    .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andReturn();

            assertEquals(404,mvcResult.getResponse().getStatus());

            String jsonMessage = mvcResult.getResponse().getContentAsString();

            assertEquals("Product not found",jsonMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}