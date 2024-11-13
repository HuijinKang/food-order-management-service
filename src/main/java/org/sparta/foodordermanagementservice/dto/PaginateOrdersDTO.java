package org.sparta.foodordermanagementservice.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.sparta.foodordermanagementservice.dto.request.PaginateOrdersReqCondition;
import org.sparta.foodordermanagementservice.dto.request.ReqPaginateOrders;
import org.sparta.foodordermanagementservice.dto.request.SortedBy;

import java.util.UUID;

@SuppressWarnings("unused")

@ToString
@Getter
@Builder
@RequiredArgsConstructor
public class PaginateOrdersDTO {

    private final UUID storeId;
    private final String userName;
    private final int pageSize;
    private final int pageNumber;
    private final SortedBy sortedBy;
    private final boolean isAsc;

    public static PaginateOrdersDTO from(ReqPaginateOrders req) {

        UUID targetStoreId
                = req.getCondition() == PaginateOrdersReqCondition.STORE_ID
                ? UUID.fromString(req.getKey())
                : null;

        String targetUserName
                = req.getCondition() == PaginateOrdersReqCondition.USER_NAME
                ? req.getKey()
                : null;

        return PaginateOrdersDTO.builder()
                .storeId(targetStoreId)
                .userName(targetUserName)
                .pageSize(req.getPageSize())
                .pageNumber(req.getPageNumber())
                .sortedBy(req.getSortedBy())
                .isAsc(req.isAsc())
                .build();
    }
}
