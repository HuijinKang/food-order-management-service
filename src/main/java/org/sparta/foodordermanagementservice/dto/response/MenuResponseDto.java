package org.sparta.foodordermanagementservice.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
public class MenuResponseDto {
    private String name;
    private int price;
//    private String menuImageUrl;
    private String description;
    private UUID storeId;

    @Builder
    public MenuResponseDto(String name, int price/*, String menuImageUrl*/, String description, UUID storeId) {
        this.name = name;
        this.price = price;
//        this.menuImageUrl = menuImageUrl;
        this.description = description;
        this.storeId = storeId;
    }
}
