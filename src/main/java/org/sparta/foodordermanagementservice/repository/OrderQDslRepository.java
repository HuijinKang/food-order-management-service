package org.sparta.foodordermanagementservice.repository;

import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.domain.Order;
import org.sparta.foodordermanagementservice.dto.SelectOrderListDto;

import java.util.List;

@SuppressWarnings("unused")

@RequiredArgsConstructor
public class OrderQDslRepository {
    public List<Order> selectOrderList(SelectOrderListDto dto) {
        return null;
    }
}
