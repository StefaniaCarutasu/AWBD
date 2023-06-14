package com.awbd.orders.daos;

import com.awbd.orders.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdersDao extends JpaRepository<Order, Long> {

    @Query(value = "select o from Order o")
    List<Order> findAll();

    @Query(value = "select o from Order o where o.id = ?1")
    Optional<Order> findById(Long id);

    @Query(value = "select o from Order o where o.username = ?1")
    List<Order> getAllByUsername(String username);
}
