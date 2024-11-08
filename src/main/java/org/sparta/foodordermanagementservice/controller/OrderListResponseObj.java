package org.sparta.foodordermanagementservice.controller;

import lombok.Builder;
import lombok.Getter;
import org.sparta.foodordermanagementservice.dto.OrderDTO;
import org.sparta.foodordermanagementservice.entity.enumerate.OrderStatus;
import org.sparta.foodordermanagementservice.entity.enumerate.OrderType;


@Getter
public class OrderListResponseObj {

    private final String storeId;
    private final OrderStatus status;
    private final OrderType type;
    private final String address;
    private final int totalPrice;


    public static OrderListResponseObj from(OrderDTO order) {

        //todo storeDTO 작업되면 수정
        return OrderListResponseObj.builder()
                .storeId("temp")      //String.valueOf(order.getStoreDTO().getId()))
                .status(order.getStatus())
                .type(order.getType())
                .address(order.getAddress())
                .totalPrice(order.getTotalPrice())
                .build();
    }


    @Builder
    public OrderListResponseObj(String storeId, OrderStatus status, OrderType type, String address, int totalPrice) {
        this.storeId = storeId;
        this.status = status;
        this.type = type;
        this.address = address;
        this.totalPrice = totalPrice;
    }
}
