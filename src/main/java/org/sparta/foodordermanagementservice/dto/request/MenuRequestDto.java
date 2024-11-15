package org.sparta.foodordermanagementservice.dto.request;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Getter
public class MenuRequestDto {
    private String name;
    private int price;
    private String description;
    private MultipartFile file;

    public MenuRequestDto(String name, int price, String description, MultipartFile file) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.file = file;
    }
}
