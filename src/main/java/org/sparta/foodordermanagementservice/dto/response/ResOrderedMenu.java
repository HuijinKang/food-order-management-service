package org.sparta.foodordermanagementservice.dto.response;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.sparta.foodordermanagementservice.dto.OrderedMenuDTO;

import java.time.LocalDateTime;
import java.util.UUID;

@ToString
@Slf4j


@Builder
@Value
public class ResOrderedMenu {

    UUID menuId;
    String menuName;
    int price;
    int amount;
    LocalDateTime createdAt;
    String createdBy;
    LocalDateTime updatedAt;
    String updatedBy;
    LocalDateTime deletedAt;
    String deletedBy;


    public static ResOrderedMenu from(OrderedMenuDTO dto) {
        return ResOrderedMenu.builder()
                .menuId(dto.getMenuId())
                .menuName(dto.getMenuName())
                .amount(dto.getAmount())
                .createdAt(dto.getCreatedAt())
                .createdBy(dto.getCreatedBy())
                .updatedAt(dto.getUpdatedAt())
                .updatedBy(dto.getUpdatedBy())
                .deletedAt(dto.getDeletedAt())
                .deletedBy(dto.getDeletedBy())

                .build();
    }

}