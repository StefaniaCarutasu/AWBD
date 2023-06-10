package com.awbd.products.controller;

import com.awbd.products.model.Product;
import com.awbd.products.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Slf4j
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    ProductService productService;

    @GetMapping(value = "/product/list", produces = {"application/hal+json"})
    public CollectionModel<Product> findAll( ) {

        List<Product> products = productService.findAll();
        for (final Product product : products) {
            Link selfLink = linkTo(methodOn(ProductController.class).getProduct(product.getId())).withSelfRel();
            product.add(selfLink);
            Link deleteLink = linkTo(methodOn(ProductController.class).deleteProduct(product.getId())).withRel("deleteProduct");
            product.add(deleteLink);
        }
        Link link = linkTo(methodOn(ProductController.class).findAll()).withSelfRel();
        CollectionModel<Product> result = CollectionModel.of(products, link);
        return result;
    }


    @PostMapping("/product")
    public ResponseEntity<Product> save(@Valid @RequestBody Product product){
        Product savedProduct = productService.save(product);
        URI locationUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{productId}").buildAndExpand(savedProduct.getId())
                .toUri();

        return ResponseEntity.created(locationUri).body(savedProduct);
    }


    @Operation(summary = "delete product by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "product deleted",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content)})
    @DeleteMapping("/subscription/{subscriptionId}")
    public Product deleteProduct(@PathVariable Long subscriptionId) {

        Product subscription = productService.delete(subscriptionId);
        return subscription;
    }

    @GetMapping("/product/{productId}")
    public Product getProduct(@PathVariable Long productId) {
        logger.info("product by id request start");
        Product product = productService.findById(productId);
        logger.info("product by id request end");
        return product;

    }


}
