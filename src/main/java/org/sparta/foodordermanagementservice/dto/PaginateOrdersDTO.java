package org.sparta.foodordermanagementservice.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.sparta.foodordermanagementservice.dto.request.SortedBy;

import java.util.UUID;

@SuppressWarnings("unused")

@ToString
@Slf4j

@Getter
public class PaginateOrdersDTO {

    private final UUID storeId;
    private final String username;
    private final int pageSize;
    private final int pageNumber;
    private final SortedBy sortedBy;
    private final boolean isAsc;

    @Builder
    public PaginateOrdersDTO(UUID storeId, String username, int pageSize, int pageNumber, SortedBy sortedBy, boolean isAsc) {

        this.storeId = storeId;
        this.username = username;
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        this.sortedBy = sortedBy;
        this.isAsc = isAsc;
    }
}