package org.sparta.foodordermanagementservice.dto.response;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import org.sparta.foodordermanagementservice.dto.OrderDTO;
import org.sparta.foodordermanagementservice.entity.enumerate.OrderStatus;
import org.sparta.foodordermanagementservice.entity.enumerate.OrderType;


@ToString

@Value
public class ResPagedOrderObj {

    String storeId;
    OrderStatus status;
    OrderType type;
    String address;
    int totalPrice;


    public static ResPagedOrderObj from(OrderDTO order) {

        //todo storeDTO 작업되면 수정
        return ResPagedOrderObj.builder()
                .storeId("temp")      //String.valueOf(order.getStoreDTO().getId()))
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
