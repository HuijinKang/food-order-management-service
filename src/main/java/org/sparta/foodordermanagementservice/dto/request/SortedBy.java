package org.sparta.foodordermanagementservice.dto.request;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SortedBy {

    UPDATED_AT("updatedAt"),
    CREATED_AT("createdAt");

    private final String requested;


    public static SortedBy of(String requested) {
        for (SortedBy sortedBy : values()) {
            if (sortedBy.requested.equals(requested)) {
                return sortedBy;
            }
        }
        throw new IllegalArgumentException("지원하지 않는 정렬 방식입니다.");
    }
}
