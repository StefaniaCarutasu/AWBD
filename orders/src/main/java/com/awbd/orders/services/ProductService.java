package com.awbd.orders.services;

import com.awbd.orders.dtod.ProductDto;
import com.awbd.orders.models.Product;

import java.util.List;

public interface ProductService {

    Product addProduct(ProductDto product);

    Product getProductById(Long productId);

    List<Product> getAllProducts();
}
