package org.sparta.foodordermanagementservice.dto;


import lombok.Getter;
import lombok.Value;
import org.sparta.foodordermanagementservice.entity.enumerate.OrderStatus;

@Value
@Getter
public class ReqUpdateOrder {

    OrderStatus orderStatus;
}