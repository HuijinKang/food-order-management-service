package org.sparta.foodordermanagementservice.dto.response;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StoreListResponse {
    private final String storeId;
    private final String userId;
    private final double latitude;
    private final double longitude;
    private final String name;
}
