package org.sparta.foodordermanagementservice.service;

import org.sparta.foodordermanagementservice.dto.PaginateOrdersDTO;
import org.sparta.foodordermanagementservice.dto.response.ResPagedOrderObj;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface OrderService {
    Page<ResPagedOrderObj> paginateOrders(PaginateOrdersDTO dto, UserDetails userDetails);


    void deleteOrder(UUID orderId,String userName);
}
