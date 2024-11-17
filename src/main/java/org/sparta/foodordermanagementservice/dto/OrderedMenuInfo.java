package org.sparta.foodordermanagementservice.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.sparta.foodordermanagementservice.dto.request.ReqOrderedMenu;

import java.util.UUID;


@Builder
@RequiredArgsConstructor
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
                .build();
    }
}
