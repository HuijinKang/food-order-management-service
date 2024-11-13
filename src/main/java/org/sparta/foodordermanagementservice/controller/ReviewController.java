package org.sparta.foodordermanagementservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.common.ApiResponse;
import org.sparta.foodordermanagementservice.dto.request.ReviewRequestDto;
import org.sparta.foodordermanagementservice.service.ReviewService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 작성
    @PostMapping
    public ApiResponse<?> createReview(@RequestParam(required = false) UUID storeId,
                                       @RequestBody @Valid ReviewRequestDto requestDto,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        reviewService.createReview(storeId, requestDto, userDetails.getUsername());
        return ApiResponse.ofSuccess(null);
    }
}