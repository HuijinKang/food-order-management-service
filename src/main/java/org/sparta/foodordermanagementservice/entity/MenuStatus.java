package org.sparta.foodordermanagementservice.entity;

import lombok.Getter;

@Getter
public enum MenuStatus {
    ACTIVE("활성화"),
    OUT_OF_STOCK("재고 소진"),
    DISCONTINUED("판매 중지");

    private final String menuStatus;

    MenuStatus(String menuStatus) {
        this.menuStatus = menuStatus;
    }
}