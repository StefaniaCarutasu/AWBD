package com.awbd.products.services;

import com.awbd.products.model.Product;

import java.util.List;

public interface ProductService {
    Product save(Product product);
    List<Product> findAll();
    Product delete(Long id);
    Product findById(Long id);
}
