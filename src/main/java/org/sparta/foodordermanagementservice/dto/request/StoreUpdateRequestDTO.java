package org.sparta.foodordermanagementservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class StoreUpdateRequestDTO {
    @NotNull(message = "지역 입력은 필수입니다.")
    private final String region;
    @NotNull(message = "위치 입력은 필수입니다.")
    private final double latitude;
    @NotNull(message = "위치 입력은 필수입니다.")
    private final double longitude;
    @NotNull(message = "가게명 입력은 필수입니다.")
    private final String name;
    @NotNull(message = "카테고리 입력은 필수입니다.")
    private final List<UUID> category;

    @Builder
    public StoreUpdateRequestDTO(String name, String region, double latitude, double longitude, List<UUID> category) {
        this.name = name;
        this.region = region;
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
    }
}
