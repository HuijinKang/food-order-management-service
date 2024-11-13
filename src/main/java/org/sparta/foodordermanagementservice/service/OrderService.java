package org.sparta.foodordermanagementservice.service;

import org.sparta.foodordermanagementservice.dto.PaginateOrdersDTO;
import org.sparta.foodordermanagementservice.dto.response.ResPagedOrderObj;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface OrderService {
    Page<ResPagedOrderObj> paginateOrders(PaginateOrdersDTO dto);

    void deleteOrder(UUID orderId);
}
