package org.sparta.foodordermanagementservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.sparta.foodordermanagementservice.entity.OrderedMenu;

import java.time.LocalDateTime;
import java.util.UUID;


@Builder
@Getter
@AllArgsConstructor
public class OrderedMenuDTO {


    private UUID menuId;
    private String menuName;
    private int price;
    private int amount;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private LocalDateTime deletedAt;
    private String deletedBy;


    public static OrderedMenuDTO from(OrderedMenu orderedMenu) {

        return OrderedMenuDTO.builder()
                .menuId(orderedMenu.getMenu().getId())
                .menuName(orderedMenu.getMenu().getName())
                .price(orderedMenu.getMenu().getPrice())
                .amount(orderedMenu.getAmount())
                .createdAt(orderedMenu.getCreatedAt())
                .createdBy(orderedMenu.getCreatedBy())
                .updatedAt(orderedMenu.getUpdatedAt())
                .updatedBy(orderedMenu.getUpdatedBy())
                .deletedAt(orderedMenu.getDeletedAt())
                .deletedBy(orderedMenu.getDeletedBy())
                .build();
    }

}
