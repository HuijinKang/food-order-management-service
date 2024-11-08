package org.sparta.foodordermanagementservice.repository;

import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.domain.Order;
import org.sparta.foodordermanagementservice.dto.SelectOrderListDto;

import java.util.List;


@RequiredArgsConstructor
@SuppressWarnings("unused")
public class OrderRepository {
    private final OrderJpaRepository orderJpaRepo;
    private final OrderQDslRepository orderQDslRepo;


    public List<Order> selectOrderList(SelectOrderListDto dto) {

        return orderQDslRepo.selectOrderList(dto);
    }
}
