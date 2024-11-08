package org.sparta.foodordermanagementservice.repository;

import org.sparta.foodordermanagementservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {

}
