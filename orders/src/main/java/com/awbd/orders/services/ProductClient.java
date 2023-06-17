package com.awbd.orders.services;

import com.awbd.orders.models.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "productClient", url = "http://localhost:8080/products")
public interface ProductClient {

    @GetMapping(value = "/{id}", consumes = "application/json")
    Product findById(@PathVariable Long id);

    @GetMapping(value = "/list", consumes = "application/json")
    List<Product> findAll();

    @GetMapping(value = "/allByIds", consumes = "application/json")
    List<Product> findAllByIds(@RequestBody List<Long> prodIds);

//    @RequestLine("GET")
//    List<Product> findAll();
//
//    @RequestLine("POST")
//    @Headers("Content-Type: application/json")
//    void create(Product product);
}
