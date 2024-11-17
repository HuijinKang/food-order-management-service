package org.sparta.foodordermanagementservice.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.common.exeption.CustomException;
import org.sparta.foodordermanagementservice.common.exeption.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum SortedBy {

    UPDATED_AT("updatedAt"),
    CREATED_AT("createdAt");

    private final String queryString;


    public static SortedBy of(String requested) {
        for (SortedBy sortedBy : values()) {
            if (sortedBy.queryString.equals(requested)) {
                return sortedBy;
            }
        }
        throw new CustomException(ErrorCode.BAD_REQUEST);
    }
}
