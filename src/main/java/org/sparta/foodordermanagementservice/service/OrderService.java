package org.sparta.foodordermanagementservice.service;

import org.sparta.foodordermanagementservice.dto.CreateOrderDto;
import org.sparta.foodordermanagementservice.dto.PaginateOrdersDTO;
import org.sparta.foodordermanagementservice.dto.UpdateOrderStatusDto;
import org.sparta.foodordermanagementservice.dto.response.ResPagedOrderObj;
import org.sparta.foodordermanagementservice.dto.response.ResReadOrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface OrderService {
    Page<ResPagedOrderObj> paginateOrders(PaginateOrdersDTO dto, UserDetails userDetails);


    void deleteOrder(UUID orderId, UserDetails userDetails);

    ResReadOrderDetail readOrderDetail(UUID orderId,UserDetails userDetails);

    UUID createOrder(CreateOrderDto dto);

    void updateOrder(UUID orderId, UpdateOrderStatusDto dto);
}
