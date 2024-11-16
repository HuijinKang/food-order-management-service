package org.sparta.foodordermanagementservice.dto.response;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.sparta.foodordermanagementservice.dto.OrderDTO;
import org.sparta.foodordermanagementservice.entity.enumerate.OrderStatus;
import org.sparta.foodordermanagementservice.entity.enumerate.OrderType;

@Slf4j
@ToString

@Value
public class ResPagedOrderObj {

    String storeId;
    OrderStatus status;
    OrderType type;
    String address;
    int totalPrice;


    public static ResPagedOrderObj from(OrderDTO order) {
        log.info("orderDto: {}", order);
        return ResPagedOrderObj.builder()
                .storeId(String.valueOf(order.getStoreId()))
                .status(order.getStatus())
                .type(order.getType())
                .address(order.getAddress())
                .totalPrice(order.getTotalPrice())
                .build();
    }

    @Builder
    public ResPagedOrderObj(String storeId, OrderStatus status, OrderType type, String address, int totalPrice) {
        this.storeId = storeId;
        this.status = status;
        this.type = type;
        this.address = address;
        this.totalPrice = totalPrice;
    }
}
