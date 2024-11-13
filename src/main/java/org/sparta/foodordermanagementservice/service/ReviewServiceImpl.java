package org.sparta.foodordermanagementservice.service;

import org.sparta.foodordermanagementservice.dto.request.ReviewRequestDto;
import org.sparta.foodordermanagementservice.entity.Review;
import org.sparta.foodordermanagementservice.entity.Store;
import org.sparta.foodordermanagementservice.entity.User;
import org.sparta.foodordermanagementservice.repository.ReviewRepository;

import java.util.UUID;

public class ReviewServiceImpl implements ReviewService {

    private ReviewRepository reviewRepository;
//    private StoreService storeService;
//    private UserService userService;

    // 리뷰 작성
    public void createReview(UUID storeId, ReviewRequestDto requestDto, String username) {
//        Store store = storeService.findByStoreId(storeId);
//        User user = userService.findByUsername(username);

        Review review = Review.builder()
//                .store(store)
//                .user(user)
                .rating(requestDto.getRating())
                .content(requestDto.getContent())
                .build();

        reviewRepository.save(review);
    }
}
