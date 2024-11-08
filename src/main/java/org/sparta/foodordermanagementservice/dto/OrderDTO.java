package org.sparta.foodordermanagementservice.dto;


import lombok.Builder;
import lombok.Getter;
import org.sparta.foodordermanagementservice.entity.Order;
import org.sparta.foodordermanagementservice.entity.enumerate.OrderStatus;
import org.sparta.foodordermanagementservice.entity.enumerate.OrderType;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@SuppressWarnings("unused")
public class OrderDTO {

    private UUID id;
    private UserDTO userDTO;
    private StoreDTO storeDTO;
    private OrderStatus status;
    private OrderType type;
    private String address;
    private String comment;
    private int totalPrice;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private LocalDateTime deletedAt;
    private String deletedBy;


    @Builder //AllArgsConstructor
    public OrderDTO(UUID id, UserDTO userDTO, StoreDTO storeDTO, OrderStatus status, OrderType type, String address, String comment, int totalPrice, LocalDateTime createdAt, String createdBy, LocalDateTime updatedAt, String updatedBy, LocalDateTime deletedAt, String deletedBy) {
        this.id = id;
        this.userDTO = userDTO;
        this.storeDTO = storeDTO;
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

    public static OrderDTO from(Order order) {
        //todo 담당자와 연계해 user, store 변환함수 builder 인자로 추가
        UserDTO userDto = new UserDTO();
        StoreDTO storeDto = new StoreDTO();

        return OrderDTO.builder()
                .id(order.getId())
                .userDTO(userDto)
                .storeDTO(storeDto)
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
}
