package org.sparta.foodordermanagementservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.sparta.foodordermanagementservice.dto.request.OrderListRequestCondition;
import org.sparta.foodordermanagementservice.dto.request.ReqReadOrderList;
import org.sparta.foodordermanagementservice.dto.request.SortedBy;

@ToString //todo test용

@Getter
public class ReadOrderListDto {

    private final OrderListRequestCondition condition;
    private final String key;
    @Setter
    private int pageSize;
    private final int pageNumber;
    private final SortedBy sortedBy;
    private final boolean isAsc;

    public static ReadOrderListDto from(ReqReadOrderList request) {

        return ReadOrderListDto.builder()
                .condition(request.getCondition())
                .key(request.getKey())
                .pageSize(request.getPageSize())
                .pageNumber(request.getPageNumber())
                .sortedBy(request.getSortedBy())
                .isAsc(request.isAsc())
                .build();
    }

    @Builder
    public ReadOrderListDto
            (
                    OrderListRequestCondition condition,
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
