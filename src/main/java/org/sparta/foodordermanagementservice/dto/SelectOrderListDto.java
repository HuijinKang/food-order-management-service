package org.sparta.foodordermanagementservice.dto;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.dto.request.OrderListRequestCondition;
import org.sparta.foodordermanagementservice.dto.request.SortedBy;
@SuppressWarnings("unused")

@RequiredArgsConstructor(staticName = "from")
@AllArgsConstructor(staticName = "from")
public class SelectOrderListDto {

    private final OrderListRequestCondition condition;
    private final Long key;
    private final int pageSize;
    private final int pageNumber;
    private final SortedBy sortedBy;
    private final boolean isAsc;


    public static SelectOrderListDto from(SearchOrderListDto dto) {
        return SelectOrderListDto.from(
                dto.getCondition(),
                dto.getKey(),
                dto.getPageSize(),
                dto.getPageNumber(),
                dto.getSortedBy(),
                dto.isAsc()
        );
    }
}
