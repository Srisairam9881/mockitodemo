package com.ust.springapp.controller;

import com.ust.springapp.model.Product;
import com.ust.springapp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestBody Product product){
        Product savedProduct = productService.addProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getProduct(@PathVariable long productId){
        Product responseProduct = productService.getProductById(productId);
        return ResponseEntity.status(HttpStatus.OK).body(responseProduct);
    }
}
