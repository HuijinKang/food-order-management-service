package org.sparta.foodordermanagementservice.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import org.sparta.foodordermanagementservice.dto.OrderDTO;
import org.sparta.foodordermanagementservice.dto.PaymentDTO;
import org.sparta.foodordermanagementservice.entity.enumerate.OrderStatus;
import org.sparta.foodordermanagementservice.entity.enumerate.OrderType;

import java.util.List;
import java.util.UUID;


@AllArgsConstructor
@Builder
public class ResReadOrderDetail {
    private final UUID storeId;
    private final String storeName;
    private final OrderStatus orderStatus;
    private final OrderType orderType;
    private final String address;
    private final int totalPrice;
    private final List<ResOrderedMenu> menuList;
    private final String comment;
    private final PaymentDTO payment;


    public static ResReadOrderDetail from(OrderDTO orderDTO,
                                          List<ResOrderedMenu> menuList,
                                          PaymentDTO payment) {

        return ResReadOrderDetail.builder()
                .storeId(orderDTO.getId())
                .storeName(orderDTO.getStoreName())
                .orderStatus(orderDTO.getStatus())
                .orderType(orderDTO.getType())
                .address(orderDTO.getAddress())
                .totalPrice(orderDTO.getTotalPrice())
                .<ResOrderedMenu>menuList(menuList)
                .comment(orderDTO.getComment())
                .payment(payment)
                .build();
    }
}
