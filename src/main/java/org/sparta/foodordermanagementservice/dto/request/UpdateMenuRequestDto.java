package org.sparta.foodordermanagementservice.dto.request;

import lombok.Getter;
import org.sparta.foodordermanagementservice.entity.MenuStatus;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class UpdateMenuRequestDto {
    private String name;
    private int price;
    private String description;
    private MenuStatus status;
    private MultipartFile updateFile;

    public UpdateMenuRequestDto(String name, int price, String description, MenuStatus status, MultipartFile updateFile) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.status = status;
        this.updateFile = updateFile;
    }
}