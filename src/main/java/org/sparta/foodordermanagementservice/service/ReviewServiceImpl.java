package org.sparta.foodordermanagementservice.service;

import org.sparta.foodordermanagementservice.dto.request.ReviewRequestDto;
import org.sparta.foodordermanagementservice.dto.response.ReviewResponseDto;
import org.sparta.foodordermanagementservice.entity.Review;
import org.sparta.foodordermanagementservice.entity.Store;
import org.sparta.foodordermanagementservice.entity.User;
import org.sparta.foodordermanagementservice.entity.UserRole;
import org.sparta.foodordermanagementservice.repository.ReviewRepository;
import org.springframework.transaction.annotation.Transactional;

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

    // 리뷰 수정
    @Transactional
    public void updateReview(UUID reviewId, ReviewRequestDto requestDto, String username) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다."));

        if (!review.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("본인이 작성한 리뷰만 수정할 수 있습니다.");
        }

        review.update(requestDto);
    }

    // 리뷰 삭제
    public void deleteReview(UUID reviewId, String username) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다."));

//        if (!review.getUser().getUsername().equals(username) && !isMaster(username)) {
//            throw new IllegalArgumentException("본인이 작성한 리뷰 또는 관리자만 삭제할 수 있습니다.");
//        }

        reviewRepository.delete(review);
    }

    // 리뷰 단건 조회
    public ReviewResponseDto getReview(UUID reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다."));

        return ReviewResponseDto.builder()
                .rating(review.getRating())
                .content(review.getContent())
                .build();
    }

    // 관리자 권한 체크 (ROLE_MASTER만 관리자)
//    private boolean isMaster(String username) {
//        User user = userService.findByUsername(username);
//        return user.getUserRole() == UserRole.MASTER;
//    }


}
