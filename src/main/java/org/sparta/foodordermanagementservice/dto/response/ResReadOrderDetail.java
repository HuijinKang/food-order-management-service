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
    private final OrderStatus status;
    private final OrderType type;
    private final String address;
    private final int totalPrice;
    private final List<ResOrderedMenu> menuList;
    private final String comment;
    private final PaymentDTO payment;


    public static ResReadOrderDetail from(OrderDTO orderDTO, List<ResOrderedMenu> menuList, PaymentDTO payment) {

        return ResReadOrderDetail.builder()
//todo 작성
                .build();

    }
}
