package org.sparta.foodordermanagementservice.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.entity.Store;
import org.sparta.foodordermanagementservice.entity.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class Order {

    //todo db보며 맞는지 체크
    private UUID id;
    private User user;
    private Store store;
    private OrderStatus status;
    private OrderType type;
    private String address;
    private String comment;
    private Integer totalPrice;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private LocalDateTime deletedAt;
    private String deletedBy;
}
