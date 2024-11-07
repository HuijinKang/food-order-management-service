package org.sparta.foodordermanagementservice.dto.response;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderListResponse {
    private final String storeId;
    private final String status;
    private final String type;
    private final String address;
    private final int totalPrice;
}
