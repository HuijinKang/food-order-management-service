package org.sparta.foodordermanagementservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.common.ApiResponse;
import org.sparta.foodordermanagementservice.dto.request.ReviewRequestDto;
import org.sparta.foodordermanagementservice.service.ReviewService;
import org.springframework.security.access.annotation.Secured;
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

    // 리뷰 수정
    @PutMapping("/{reviewId}")
    public ApiResponse<?> updateReview(@PathVariable UUID reviewId,
                                       @Valid @RequestBody ReviewRequestDto requestDto,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        reviewService.updateReview(reviewId, requestDto, userDetails.getUsername());
        return ApiResponse.ofSuccess(null);
    }

    // 리뷰 삭제
    @Secured({"ROLE_CUSTOMER", "ROLE_MASTER"})
    @DeleteMapping("/{reviewId}")
    public ApiResponse<?> deleteReview(@PathVariable UUID reviewId,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        // 리뷰 삭제 서비스 호출
        reviewService.deleteReview(reviewId, userDetails.getUsername());
        return ApiResponse.ofSuccess(null);
    }
}