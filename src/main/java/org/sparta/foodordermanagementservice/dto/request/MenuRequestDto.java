package org.sparta.foodordermanagementservice.dto.request;

import lombok.Getter;

@Getter
public class MenuRequestDto {
    private String name;
    private int price;
    private String description;

    public MenuRequestDto(String name, int price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }
}
