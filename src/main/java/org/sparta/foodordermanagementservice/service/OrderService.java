package org.sparta.foodordermanagementservice.service;

import org.sparta.foodordermanagementservice.dto.OrderDTO;
import org.sparta.foodordermanagementservice.dto.SearchOrderListDTO;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    List<OrderDTO> searchOrderList(SearchOrderListDTO dto);

    void deleteOrder(UUID id);
}
