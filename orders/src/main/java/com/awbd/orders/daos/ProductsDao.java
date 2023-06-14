package com.awbd.orders.daos;

import com.awbd.orders.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductsDao extends JpaRepository<Product, Long> {

    @Query(value = "select p from Product p")
    List<Product> findAll();

    @Query(value = "select p from Product p where p.id = ?1")
    Optional<Product> findById(Long id);
}
