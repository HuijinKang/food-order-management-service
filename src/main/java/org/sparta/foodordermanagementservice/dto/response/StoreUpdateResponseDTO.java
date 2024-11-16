package org.sparta.foodordermanagementservice.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
public class StoreUpdateResponseDTO {
    private final UUID id;
    private final String name;
    private final String region;
    private final double latitude;
    private final double longitude;
    private final Set<String> categories;
    private final LocalDateTime updatedAt;
    private final String updatedBy;

    @Builder
    public StoreUpdateResponseDTO(LocalDateTime updatedAt, UUID id, String name, String region, double latitude, double longitude, Set<String> categories, String updatedBy) {
        this.updatedAt = updatedAt;
        this.id = id;
        this.name = name;
        this.region = region;
        this.latitude = latitude;
        this.longitude = longitude;
        this.categories = categories;
        this.updatedBy = updatedBy;
    }
}
