package com.awbd.orders.services.impl;

import com.awbd.orders.daos.ProductsDao;
import com.awbd.orders.dtod.ProductDto;
import com.awbd.orders.exceptions.ProductNotFoundException;
import com.awbd.orders.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.awbd.orders.services.ProductService;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductsDao productsDao;

    @Override
    public Product addProduct(ProductDto product) {
        Product newProduct = new Product(product);
        return productsDao.save(newProduct);
    }

    @Override
    public Product getProductById(Long productId) {
        Optional<Product> productOptional = productsDao.findById(productId);
        if (productOptional.isPresent()) {
            return productOptional.get();
        } else {
            throw new ProductNotFoundException("Product with id " + productId + " not found");
        }
    }

    @Override
    public List<Product> getAllProducts() {
        return productsDao.findAll();
    }

//    @Override
//    public List<Product> getAllProductsWithClient() {
//        return productClient.findAll();
//    }
//
//    @Override
//    public Product getProductByIdWithClient(Long prodId) {
//        return productClient.findById(prodId);
//    }
}
