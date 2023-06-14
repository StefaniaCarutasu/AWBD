package com.awbd.orders.models;


import com.awbd.orders.dtod.ProductDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private Double price;

    @ManyToMany(mappedBy = "products")
    private List<Order> orders;

    public Product(ProductDto productDto) {
        this.name = productDto.getName();
        this.price = productDto.getPrice();
    }
}
