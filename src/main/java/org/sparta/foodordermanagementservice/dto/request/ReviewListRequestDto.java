package org.sparta.foodordermanagementservice.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewListRequestDto {
    private int pageNumber = 0; // 기본값 설정
    private int pageSize = 10; // 기본값 설정
    private boolean isAsc = true;
}
