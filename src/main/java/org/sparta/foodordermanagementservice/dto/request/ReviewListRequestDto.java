package org.sparta.foodordermanagementservice.dto.request;

import lombok.Getter;

@Getter
public class ReviewListRequestDto {
    private int pageNumber;
    private int pageSize;
    private boolean isAsc;
}
