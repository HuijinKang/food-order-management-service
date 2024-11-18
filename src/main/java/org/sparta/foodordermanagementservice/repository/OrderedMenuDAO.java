package org.sparta.foodordermanagementservice.repository;

import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.entity.OrderedMenu;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
@RequiredArgsConstructor
public class OrderedMenuDAO {

    private final OrderedMenuJpaRepository jpaRepo;

    public List<OrderedMenu> readOrderedMenuList(UUID orderId) {
        return jpaRepo.findAllById(orderId);
    }

    public OrderedMenu create(OrderedMenu orderedMenu) {
        return jpaRepo.save(orderedMenu);
    }
}
