package com.example.springbootyuzu.service;

import com.example.springbootyuzu.domain.Product;
import com.example.springbootyuzu.domain.ProductRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAll(){
        return productRepository.findAll();
    }
    public Product save(Product product){
        return productRepository.save(product);
    }

    public Product getOne(Integer productId){
        return productRepository.getOne(productId);
    }

    public void deleteOne(Integer productId){
        productRepository.deleteById(productId);
    }
}
