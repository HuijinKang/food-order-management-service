package org.sparta.foodordermanagementservice.repository;

import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.dto.OrderedMenuDTO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class OrderedMenuRepository {

    private final OrderedMenuDAO dao;


    public List<OrderedMenuDTO> readOrderedMenuList(UUID orderId) {

        return dao.readOrderedMenuList(orderId)
                .stream()
                .map(OrderedMenuDTO::from)
                .toList();


    }
}
