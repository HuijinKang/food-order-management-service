package org.sparta.foodordermanagementservice.dto.request;

import lombok.Getter;

@Getter
public class SearchRequestDto {

    private String condition;
    private String keyword;
    private int pageSize;
    private int pageNumber;
    private String sortedBy;
    private boolean isAsc;

    public SearchRequestDto(String condition, String keyword, int pageSize, int pageNumber, String sortedBy, boolean isAsc) {
        this.condition = condition;
        this.keyword = keyword;
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        this.sortedBy = sortedBy;
        this.isAsc = isAsc;
    }
}

