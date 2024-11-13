package org.sparta.foodordermanagementservice.dto.request;

import lombok.Value;


@Value
public class ReqReadOrderList {

    OrderListRequestCondition condition;
    String key;
    int pageSize;
    int pageNumber;
    SortedBy sortedBy;
    boolean isAsc;

}
