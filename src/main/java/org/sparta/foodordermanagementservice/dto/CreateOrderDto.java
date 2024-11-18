package org.sparta.foodordermanagementservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import org.sparta.foodordermanagementservice.dto.request.ReqCreateOrder;
import org.sparta.foodordermanagementservice.entity.enumerate.OrderType;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Value
public class CreateOrderDto {

    List<OrderedMenuInfo> orderedMenuInfos;
    UUID storeId;
    OrderType type;
    String address;
    String comment;
    String username;
    OrderType orderType;
    int totalPrice;


    public static CreateOrderDto from(ReqCreateOrder request, UserDetails userDetails) {

        List<OrderedMenuInfo> menuList
                = request.getMenuList().stream()
                .map(OrderedMenuInfo::from)
                .toList();

        return CreateOrderDto.builder()
                .orderedMenuInfos(menuList)
                .storeId(request.getStoreId())
                .type(request.getType())
                .address(request.getAddress())
                .comment(request.getComment())
                .username(userDetails.getUsername())
                .orderType(request.getOrderType())
                .totalPrice(request.getTotalPrice())
                .build();
    }
}
