package org.sparta.foodordermanagementservice.common.utils;

import org.sparta.foodordermanagementservice.entity.enumerate.OrderStatus;

public class OrderStatusConverter extends QueryStringEnumConverter<OrderStatus> {
    public OrderStatusConverter() {
        super(OrderStatus.class);
    }
}
