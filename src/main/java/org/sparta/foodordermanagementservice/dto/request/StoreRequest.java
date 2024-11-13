package org.sparta.foodordermanagementservice.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class StoreRequest {
    private String region;
    private double latitude;
    private double longitude;
    private String name;
    private List<UUID> category; // 카테고리 ID 목록
}