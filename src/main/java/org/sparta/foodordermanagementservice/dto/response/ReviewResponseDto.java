package org.sparta.foodordermanagementservice.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReviewResponseDto {
    private int rating;
    private String content;

    @Builder
    public ReviewResponseDto(int rating, String content) {
        this.rating = rating;
        this.content = content;
    }
}
