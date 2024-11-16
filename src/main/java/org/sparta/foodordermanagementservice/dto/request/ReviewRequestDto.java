package org.sparta.foodordermanagementservice.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReviewRequestDto {
    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    private int rating;
    private String content;

    public ReviewRequestDto(int rating, String content) {
        this.rating = rating;
        this.content = content;
    }
}
