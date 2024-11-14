package org.sparta.foodordermanagementservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class StoreUpdateResponseDTO {
    private UUID id;
    private String name;
    private String region;
    private double latitude;
    private double longitude;
    private Set<String> categories;
    private LocalDateTime updatedAt;
    private String updatedBy;
}
