package org.sparta.foodordermanagementservice.dto;

import lombok.Builder;
import lombok.Getter;
import org.sparta.foodordermanagementservice.dto.request.OrderListRequestCondition;
import org.sparta.foodordermanagementservice.dto.request.SortedBy;


@Getter
public class SearchOrderListDto {

    private final OrderListRequestCondition condition;
    private final Long key;
    private final int pageSize;
    private final int pageNumber;
    private final SortedBy sortedBy;
    private final boolean isAsc;

    @Builder
    public SearchOrderListDto
            (
                    OrderListRequestCondition condition,
                    Long key,
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
