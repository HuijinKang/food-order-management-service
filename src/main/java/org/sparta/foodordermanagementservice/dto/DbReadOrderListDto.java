package org.sparta.foodordermanagementservice.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.dto.request.PaginateOrdersReqCondition;
import org.sparta.foodordermanagementservice.dto.request.SortedBy;

import java.util.UUID;

@SuppressWarnings("unused")

@Getter
@RequiredArgsConstructor(staticName = "from")
public class DbReadOrderListDto {

    private final UUID storeId;
    private final String userName;
    private final int pageSize;
    private final int pageNumber;
    private final SortedBy sortedBy;
    private final boolean isAsc;

    public static DbReadOrderListDto from(ReadOrderListDto dto) {

        UUID targetStoreId
                = dto.getCondition() == PaginateOrdersReqCondition.STORE_ID
                ? UUID.fromString(dto.getKey())
                : null;

        String targetUserName
                = dto.getCondition() == PaginateOrdersReqCondition.USER_NAME
                ? dto.getKey()
                : null;

        return DbReadOrderListDto.from(
                targetStoreId,
                targetUserName,
                dto.getPageSize(),
                dto.getPageNumber(),
                dto.getSortedBy(),
                dto.isAsc()
        );
    }
}
