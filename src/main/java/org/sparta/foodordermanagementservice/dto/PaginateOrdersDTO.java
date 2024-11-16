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

//    public static PaginateOrdersDTO from(ReqPaginateOrders.java req) {
//
//        UUID targetStoreId
//                = req.getCondition() == PaginateOrdersReqCondition.STORE_ID
//                ? UUID.fromString(req.getKey())
//                : null;
//
//        String targetUserName
//                = req.getCondition() == PaginateOrdersReqCondition.USER_NAME
//                ? req.getKey()
//                : null;
//
//        return PaginateOrdersDTO.builder()
//                .storeId(targetStoreId)
//                .username(targetUserName)
//                .pageSize(req.getPageSize())
//                .pageNumber(req.getPageNumber())
//                .sortedBy(req.getSortedBy())
//                .isAsc(req.isAsc())
//                .build();
//    }
}
