package org.sparta.foodordermanagementservice.dto.request;

import lombok.Getter;
import org.sparta.foodordermanagementservice.entity.MenuStatus;

@Getter
public class UpdateMenuRequestDto {
    private String name;
    private int price;
    private String description;
    private MenuStatus status;

    public UpdateMenuRequestDto(String name, int price, String description, MenuStatus status) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.status = status;
    }
}