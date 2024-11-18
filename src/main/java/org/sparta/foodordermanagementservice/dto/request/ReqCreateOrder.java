package org.sparta.foodordermanagementservice.dto.request;


import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;
import org.sparta.foodordermanagementservice.common.utils.OrderTypeConverter;
import org.sparta.foodordermanagementservice.entity.enumerate.OrderType;

import java.util.List;
import java.util.UUID;

@ToString


@Getter
@Value
public class ReqCreateOrder {

    List<ReqOrderedMenu> menuList;
    UUID storeId;
    OrderType type;
    String address;
    String comment;
    OrderType orderType;
    int totalPrice;

    public final static OrderTypeConverter orderTypeConverter = new OrderTypeConverter();

    @JsonCreator
    @Builder
    public ReqCreateOrder(List<ReqOrderedMenu> menuList,
                          UUID storeId,
                          OrderType type,
                          String address,
                          String comment,
                          String orderType,
                          int totalPrice) {
        this.menuList = menuList;
        this.storeId = storeId;
        this.type = type;
        this.address = address;
        this.comment = comment;
        this.orderType = orderTypeConverter.convert(orderType);
        this.totalPrice = totalPrice;
    }
}
