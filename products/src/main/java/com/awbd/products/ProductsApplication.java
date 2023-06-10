package com.awbd.products;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.hateoas.config.EnableHypermediaSupport;

@SpringBootApplication
@RefreshScope
@EnableDiscoveryClient
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class ProductsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductsApplication.class, args);
    }

}
