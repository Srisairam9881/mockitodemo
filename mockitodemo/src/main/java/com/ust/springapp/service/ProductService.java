package com.ust.springapp.service;

import com.ust.springapp.exceptions.ProductNotFoundException;
import com.ust.springapp.model.Product;
import com.ust.springapp.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product addProduct(Product product){
        return productRepository.save(product);
    }

    public Product getProductById(long productId){
        Optional<Product> optional = productRepository.findById(productId);
        if(optional.isPresent()){
            return optional.get();
        }else{
            throw new ProductNotFoundException();
        }
    }
}
