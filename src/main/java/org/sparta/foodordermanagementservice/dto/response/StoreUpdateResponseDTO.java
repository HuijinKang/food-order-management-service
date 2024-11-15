package org.sparta.foodordermanagementservice.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
public class StoreUpdateResponseDTO {
    private UUID id;
    private String name;
    private String region;
    private double latitude;
    private double longitude;
    private Set<String> categories;
    private LocalDateTime updatedAt;
    private String updatedBy;

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
