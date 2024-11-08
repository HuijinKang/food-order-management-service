package org.sparta.foodordermanagementservice.service;

import org.sparta.foodordermanagementservice.domain.Order;
import org.sparta.foodordermanagementservice.dto.SearchOrderListDto;

import java.util.List;

public interface OrderService {
    List<Order> searchOrderList(SearchOrderListDto dto);
}
