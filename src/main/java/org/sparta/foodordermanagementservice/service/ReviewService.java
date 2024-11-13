package org.sparta.foodordermanagementservice.service;

import org.sparta.foodordermanagementservice.dto.request.ReviewRequestDto;
import org.sparta.foodordermanagementservice.dto.response.ReviewResponseDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface ReviewService {
    void createReview(UUID storeId, ReviewRequestDto requestDto, String username);

    void updateReview(UUID reviewId, ReviewRequestDto requestDto, String username);

    void deleteReview(UUID reviewId, String username);

    ReviewResponseDto getReview(UUID reviewId);
}
