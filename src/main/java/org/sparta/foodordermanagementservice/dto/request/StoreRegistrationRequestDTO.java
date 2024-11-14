package org.sparta.foodordermanagementservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class StoreRegistrationRequestDTO {
    @NotNull(message = "지역 입력은 필수입니다.")
    private String region;
    @NotNull(message = "위치 입력은 필수입니다.")
    private double latitude;
    @NotNull(message = "위치 입력은 필수입니다.")
    private double longitude;
    @NotNull(message = "가게명 입력은 필수입니다.")
    private String name;
    @NotNull(message = "카테고리 입력은 필수입니다.")
    private List<UUID> category;
}