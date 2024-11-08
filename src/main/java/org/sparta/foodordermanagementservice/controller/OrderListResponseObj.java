package org.sparta.foodordermanagementservice.controller;

import lombok.Builder;
import lombok.Getter;
import org.sparta.foodordermanagementservice.domain.Order;
import org.sparta.foodordermanagementservice.domain.OrderStatus;
import org.sparta.foodordermanagementservice.domain.OrderType;


@Getter
public class OrderListResponseObj {

    private final String storeId;
    private final OrderStatus status;
    private final OrderType type;
    private final String address;
    private final int totalPrice;


    public static OrderListResponseObj from(Order order) {
        return new OrderListResponseObjBuilder()
                .storeId(String.valueOf(order.getStore().getId()))
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
