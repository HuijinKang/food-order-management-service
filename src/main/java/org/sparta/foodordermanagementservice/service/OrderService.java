package org.sparta.foodordermanagementservice.service;

import org.sparta.foodordermanagementservice.dto.OrderDTO;
import org.sparta.foodordermanagementservice.dto.ReadOrderListDto;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    List<OrderDTO> readOrderList(ReadOrderListDto dto);

    void deleteOrder(UUID orderId);
}
