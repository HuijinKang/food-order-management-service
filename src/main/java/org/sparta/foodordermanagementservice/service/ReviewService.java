package org.sparta.foodordermanagementservice.service;

import org.sparta.foodordermanagementservice.dto.request.ReviewListRequestDto;
import org.sparta.foodordermanagementservice.dto.request.ReviewRequestDto;
import org.sparta.foodordermanagementservice.dto.response.ReviewResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface ReviewService {
    void createReview(UUID storeId, ReviewRequestDto requestDto, String username);

    void updateReview(UUID reviewId, ReviewRequestDto requestDto, String username);

    void deleteReview(UUID reviewId, String username, boolean isMaster);

    ReviewResponseDto getReview(UUID reviewId);

    Page<ReviewResponseDto> getReviewList(UUID storeId, ReviewListRequestDto requestDto);
}
