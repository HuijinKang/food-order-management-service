package org.sparta.foodordermanagementservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString
@Getter
@AllArgsConstructor
public class ReqPaginateOrders {

    private final PaginateOrdersReqCondition condition;
    private final String key;
    @Setter
    private int pageSize;
    private final int pageNumber;
    private final SortedBy sortedBy;
    private final boolean isAsc;
}
