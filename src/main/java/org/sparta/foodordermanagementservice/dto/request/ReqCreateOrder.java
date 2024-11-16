package org.sparta.foodordermanagementservice.dto.request;


import lombok.Builder;
import lombok.Value;
import org.sparta.foodordermanagementservice.entity.enumerate.OrderType;

import java.util.List;

@Value
@Builder
public class ReqCreateOrder {

    List<ReqOrderedMenu> menuList;
    OrderType type;
    String address;
    String comment;
}
