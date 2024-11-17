package org.sparta.foodordermanagementservice.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import org.sparta.foodordermanagementservice.dto.request.ReqOrderedMenu;

import java.util.UUID;


@Getter
@Value
public class OrderedMenuInfo {

    UUID menuId;
    int amount;
    int menuPrice;


    public static OrderedMenuInfo from(ReqOrderedMenu reqOrderedMenu) {

        return OrderedMenuInfo.builder()
                .menuId(reqOrderedMenu.getMenuId())
                .amount(reqOrderedMenu.getAmount())
                .menuPrice(reqOrderedMenu.getMenuPrice())
                .build();
    }


    @Builder
    public OrderedMenuInfo(UUID menuId, int amount, int menuPrice) {
        this.menuId = menuId;
        this.amount = amount;
        this.menuPrice = menuPrice;
    }
}
