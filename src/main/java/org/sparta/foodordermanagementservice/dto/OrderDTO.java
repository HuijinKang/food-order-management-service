package org.sparta.foodordermanagementservice.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.sparta.foodordermanagementservice.entity.Order;
import org.sparta.foodordermanagementservice.entity.enumerate.OrderStatus;
import org.sparta.foodordermanagementservice.entity.enumerate.OrderType;

import java.time.LocalDateTime;
import java.util.UUID;

@ToString

@Getter
@SuppressWarnings("unused")
public class OrderDTO {

    private UUID id;
    private String username;
    private UUID storeId;
    private String storeName;
    private OrderStatus status;
    private OrderType type;
    private String address;
    private String comment;
    private int totalPrice;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    @Setter
    private LocalDateTime deletedAt;
    @Setter
    private String deletedBy;


    public static OrderDTO from(Order order) {

        return OrderDTO.builder()
                .id(order.getId())
                .username(order.getUser().getUsername())
                .storeId(order.getId())
                .storeName(order.getStore().getName())
                .status(order.getStatus())
                .type(order.getType())
                .address(order.getAddress())
                .comment(order.getComment())
                .totalPrice(order.getTotalPrice())
                .createdAt(order.getCreatedAt())
                .createdBy(order.getCreatedBy())
                .updatedAt(order.getUpdatedAt())
                .updatedBy(order.getUpdatedBy())
                .deletedAt(order.getDeletedAt())
                .deletedBy(order.getDeletedBy())
                .build();
    }

    @Builder //AllArgsConstructor
    public OrderDTO(UUID id,
                    String username,
                    UUID storeId,
                    String storeName,
                    OrderStatus status,
                    OrderType type,
                    String address,
                    String comment,
                    int totalPrice,
                    LocalDateTime createdAt,
                    String createdBy,
                    LocalDateTime updatedAt,
                    String updatedBy,
                    LocalDateTime deletedAt,
                    String deletedBy) {
        this.id = id;
        this.username = username;
        this.storeId = storeId;
        this.storeName = storeName;
        this.status = status;
        this.type = type;
        this.address = address;
        this.comment = comment;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
        this.deletedAt = deletedAt;
        this.deletedBy = deletedBy;
    }

}
