package org.sparta.foodordermanagementservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import org.sparta.foodordermanagementservice.dto.request.SortedBy;
import org.springframework.data.domain.Pageable;

@Value
@Getter
public class PaginatePaymentsDTO {
    String username;
    int pageSize;
    int pageNumber;
    SortedBy sortedBy;
    boolean isAsc;
    boolean includingDeleted;
    Pageable pageRequest;


    @Builder
    public PaginatePaymentsDTO(String username,
                               int pageSize,
                               int pageNumber,
                               SortedBy sortedBy,
                               boolean isAsc,
                               Boolean includingDeleted,
                               Pageable pageRequest) {
        this.username = username;
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        this.sortedBy = sortedBy;
        this.isAsc = isAsc;
        this.includingDeleted = includingDeleted;
        this.pageRequest = pageRequest;
    }
}
