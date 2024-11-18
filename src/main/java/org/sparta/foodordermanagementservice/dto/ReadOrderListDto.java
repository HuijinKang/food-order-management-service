package org.sparta.foodordermanagementservice.dto;

import lombok.Builder;
import lombok.Getter;
import org.sparta.foodordermanagementservice.dto.request.PaginateOrdersReqCondition;
import org.sparta.foodordermanagementservice.dto.request.SortedBy;


@Getter
public class ReadOrderListDto {

    private final PaginateOrdersReqCondition condition;
    private final String key;
    private final int pageSize;
    private final int pageNumber;
    private final SortedBy sortedBy;
    private final boolean isAsc;

    @Builder
    public ReadOrderListDto
            (
                    PaginateOrdersReqCondition condition,
                    String key,
                    int pageSize,
                    int pageNumber,
                    SortedBy sortedBy,
                    boolean isAsc
            ) {
        this.condition = condition;
        this.key = key;
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        this.sortedBy = sortedBy;
        this.isAsc = isAsc;
    }
}
