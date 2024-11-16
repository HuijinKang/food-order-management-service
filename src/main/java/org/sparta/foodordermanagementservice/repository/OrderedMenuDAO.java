package org.sparta.foodordermanagementservice.repository;

import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.entity.OrderedMenu;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
@RequiredArgsConstructor
public class OrderedMenuDAO {

    private final OrderedMenuJpaRepository jpaRepository;

    public List<OrderedMenu> readOrderedMenuList(UUID orderId) {
        return jpaRepository.findAllById(orderId);
    }
}
