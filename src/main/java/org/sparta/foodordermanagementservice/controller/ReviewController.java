package org.sparta.foodordermanagementservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.common.ApiResponse;
import org.sparta.foodordermanagementservice.dto.request.ReviewListRequestDto;
import org.sparta.foodordermanagementservice.dto.request.ReviewRequestDto;
import org.sparta.foodordermanagementservice.dto.response.ReviewResponseDto;
import org.sparta.foodordermanagementservice.entity.UserRole;
import org.sparta.foodordermanagementservice.service.ReviewService;
import org.springframework.data.domain.Page;
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
    public ApiResponse<?> createReview(@RequestParam UUID storeId,
                                       @RequestBody @Valid ReviewRequestDto requestDto,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        reviewService.createReview(storeId, requestDto, userDetails.getUsername());
        return ApiResponse.ofSuccess(null);
    }

    // 리뷰 수정
    @PatchMapping("/{reviewId}")
    public ApiResponse<?> updateReview(@PathVariable UUID reviewId,
                                       @Valid @RequestBody ReviewRequestDto requestDto,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        reviewService.updateReview(reviewId, requestDto, userDetails.getUsername());
        return ApiResponse.ofSuccess(null);
    }

    // 리뷰 삭제
    @Secured({UserRole.Authority.CUSTOMER, UserRole.Authority.MASTER})
    @DeleteMapping("/{reviewId}")
    public ApiResponse<?> deleteReview(@PathVariable UUID reviewId,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        reviewService.deleteReview(reviewId, userDetails.getUsername());
        return ApiResponse.ofSuccess(null);
    }

    // 리뷰 단건 조회
    @GetMapping("/{reviewId}")
    public ApiResponse<?> getReview(@PathVariable UUID reviewId) {
        ReviewResponseDto responseDto = reviewService.getReview(reviewId);
        return ApiResponse.ofSuccess(responseDto);
    }

    // 가게 리뷰 목록 조회
    @GetMapping
    public ApiResponse<?> getReviewList(@RequestParam UUID storeId,
                                        @ModelAttribute ReviewListRequestDto requestDto) {
        Page<ReviewResponseDto> responsePage = reviewService.getReviewList(storeId, requestDto);
        return ApiResponse.ofSuccess(responsePage);
    }
}