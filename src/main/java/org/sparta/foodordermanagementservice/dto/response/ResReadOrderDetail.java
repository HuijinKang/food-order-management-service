package org.sparta.foodordermanagementservice.dto.response;


import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.sparta.foodordermanagementservice.dto.OrderDTO;
import org.sparta.foodordermanagementservice.dto.PaymentDTO;
import org.sparta.foodordermanagementservice.entity.enumerate.OrderStatus;
import org.sparta.foodordermanagementservice.entity.enumerate.OrderType;

import java.util.List;
import java.util.UUID;


@ToString


@Builder
@Getter
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
                .storeId(orderDTO.getStoreId())
                .storeName(orderDTO.getStoreName())
                .orderStatus(orderDTO.getStatus())
                .orderType(orderDTO.getType())
                .address(orderDTO.getAddress())
                .totalPrice(orderDTO.getTotalPrice())
                .menuList(menuList)
                .comment(orderDTO.getComment())
                .payment(payment)
                .build();
    }

    @JsonCreator

    public ResReadOrderDetail(UUID storeId, String storeName, OrderStatus orderStatus, OrderType orderType, String address, int totalPrice, List<ResOrderedMenu> menuList, String comment, PaymentDTO payment) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.orderStatus = orderStatus;
        this.orderType = orderType;
        this.address = address;
        this.totalPrice = totalPrice;
        this.menuList = menuList;
        this.comment = comment;
        this.payment = payment;
    }
}
