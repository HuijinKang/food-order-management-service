package org.sparta.foodordermanagementservice.dto;


import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.dto.request.ReqOrderedMenu;


@Builder
@RequiredArgsConstructor
public class CreateOrderedMenuDto {

    private final Long menuId;
    private final int amount;


    public static CreateOrderedMenuDto from(ReqOrderedMenu reqOrderedMenu) {

        return CreateOrderedMenuDto.builder()
                .menuId(reqOrderedMenu.getMenuId())
                .amount(reqOrderedMenu.getAmount())
                .build();
    }
}
