package org.sparta.foodordermanagementservice.common.utils;


import org.sparta.foodordermanagementservice.entity.enumerate.OrderType;

public class OrderTypeConverter extends QueryStringEnumConverter<OrderType> {

    public OrderTypeConverter() {
        super(OrderType.class);
    }

}
