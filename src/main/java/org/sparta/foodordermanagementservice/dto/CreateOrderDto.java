package org.sparta.foodordermanagementservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.dto.request.ReqCreateOrder;
import org.sparta.foodordermanagementservice.entity.enumerate.OrderType;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Builder
public class CreateOrderDto {

    private final List<CreateOrderedMenuDto> menuList;
    private final OrderType type;
    private final String address;
    private final String comment;
    private int totalPrice;


    public static CreateOrderDto from(ReqCreateOrder request) {

        List<CreateOrderedMenuDto> menuList
                = request.getMenuList().stream()
                .map(CreateOrderedMenuDto::from)
                .toList();

        return CreateOrderDto.builder()
                .menuList(menuList)
                .type(request.getType())
                .address(request.getAddress())
                .comment(request.getComment())
                .build();
    }
}
