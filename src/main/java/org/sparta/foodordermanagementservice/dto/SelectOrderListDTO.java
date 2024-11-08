package org.sparta.foodordermanagementservice.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.dto.request.OrderListRequestCondition;
import org.sparta.foodordermanagementservice.dto.request.SortedBy;

import java.util.UUID;

@SuppressWarnings("unused")

@Getter
@RequiredArgsConstructor(staticName = "from")
public class SelectOrderListDTO {

    private final UUID storeId;
    private final String userName;
    private final int pageSize;
    private final int pageNumber;
    private final SortedBy sortedBy;
    private final boolean isAsc;

    public static SelectOrderListDTO from(SearchOrderListDTO dto) {

        UUID targetStoreId
                = dto.getCondition() == OrderListRequestCondition.STORE_ID
                ? UUID.fromString(dto.getKey())
                : null;

        String targetUserName
                = dto.getCondition() == OrderListRequestCondition.USER_NAME
                ? dto.getKey()
                : null;

        return SelectOrderListDTO.from(
                targetStoreId,
                targetUserName,
                dto.getPageSize(),
                dto.getPageNumber(),
                dto.getSortedBy(),
                dto.isAsc()
        );
    }
}
