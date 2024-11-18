package org.sparta.foodordermanagementservice.repository;

import org.sparta.foodordermanagementservice.entity.OrderedMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderedMenuJpaRepository extends JpaRepository<OrderedMenu, UUID> {
    List<OrderedMenu> findAllById(UUID orderId);

}
