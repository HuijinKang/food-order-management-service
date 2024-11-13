package org.sparta.foodordermanagementservice.service;

import org.sparta.foodordermanagementservice.dto.request.ReviewRequestDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface ReviewService {
    void createReview(UUID storeId, ReviewRequestDto requestDto, String username);
}
