package com.awbd.orders.controllers;

import com.awbd.orders.dtod.ProductDto;
import com.awbd.orders.exceptions.ProductNotFoundException;
import com.awbd.orders.models.Product;
import com.awbd.orders.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @Operation(summary = "Get all products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductDto.class))}),
            @ApiResponse(responseCode = "500", description = "No products found",
                    content = @Content)})
    @GetMapping("")
    public ResponseEntity<Object> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        if (Boolean.FALSE.equals(products.isEmpty()))  {
            List<ProductDto> dtos = products.stream().map(ProductDto::new).collect(Collectors.toList());
            return new ResponseEntity<>(dtos, HttpStatusCode.valueOf(200));
        }
        return new ResponseEntity<>(HttpStatusCode.valueOf(400));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getProductById(@PathVariable Long productId) {
        try {
            Product product = productService.getProductById(productId);

            return new ResponseEntity<>(product, HttpStatusCode.valueOf(200));
        } catch (ProductNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(400));
        }
    }

    @Operation(summary = "Add new product")
    @PostMapping("/add")
    public ResponseEntity<Object> addProduct(@RequestBody ProductDto productDto) {
        if (productDto != null) {
            Product newProd = productService.addProduct(productDto);
            if (newProd != null) {
                return new ResponseEntity<>(HttpStatusCode.valueOf(200));
            }
        }
        return new ResponseEntity<>(HttpStatusCode.valueOf(500));
    }

}
