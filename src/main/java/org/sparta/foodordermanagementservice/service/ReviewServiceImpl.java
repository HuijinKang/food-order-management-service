package org.sparta.foodordermanagementservice.service;

import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.common.exeption.CustomException;
import org.sparta.foodordermanagementservice.common.exeption.ErrorCode;
import org.sparta.foodordermanagementservice.dto.request.ReviewListRequestDto;
import org.sparta.foodordermanagementservice.dto.request.ReviewRequestDto;
import org.sparta.foodordermanagementservice.dto.response.ReviewResponseDto;
import org.sparta.foodordermanagementservice.entity.Review;
import org.sparta.foodordermanagementservice.entity.Store;
import org.sparta.foodordermanagementservice.entity.User;
import org.sparta.foodordermanagementservice.entity.UserRole;
import org.sparta.foodordermanagementservice.repository.ReviewRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final StoreService storeService;
    private final UserService userService;

    // 리뷰 작성
    public void createReview(UUID storeId, ReviewRequestDto requestDto, String username) {
        Store store = storeService.findByStoreId(storeId);
        User user = userService.findByUsername(username);

        Review review = Review.builder()
                .store(store)
                .user(user)
                .rating(requestDto.getRating())
                .content(requestDto.getContent())
                .build();

        reviewRepository.save(review);
    }

    // 리뷰 수정
    @Transactional
    public void updateReview(UUID reviewId, ReviewRequestDto requestDto, String username) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        if (!review.getUser().getUsername().equals(username)) {
            throw new CustomException(ErrorCode.REVIEW_PERMISSION_DENIED);
        }

        review.update(requestDto);
    }

    // 리뷰 삭제
    public void deleteReview(UUID reviewId, String username) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        if (!review.getUser().getUsername().equals(username) && !isMaster(username)) {
            throw new CustomException(ErrorCode.REVIEW_PERMISSION_DENIED);
        }

        reviewRepository.delete(review);
    }

    // 리뷰 단건 조회
    public ReviewResponseDto getReview(UUID reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        return ReviewResponseDto.builder()
                .rating(review.getRating())
                .content(review.getContent())
                .build();
    }

    // 가게 리뷰 목록 조회
    public Page<ReviewResponseDto> getReviewList(UUID storeId, ReviewListRequestDto requestDto) {
        Pageable pageable = PageRequest.of(requestDto.getPageNumber(), requestDto.getPageSize(),
                requestDto.isAsc() ? Sort.by("createdAt").ascending() : Sort.by("createdAt").descending());


        Page<Review> reviewPage = reviewRepository.findByStoreId(storeId, pageable);

        List<ReviewResponseDto> reviewList = reviewPage.getContent().stream()
                .map(review -> ReviewResponseDto.builder()
                        .rating(review.getRating())
                        .content(review.getContent())
                        .build())
                .collect(Collectors.toList());

        return new PageImpl<>(reviewList, pageable, reviewPage.getTotalElements());
    }

    // 관리자 권한 체크 (ROLE_MASTER만 관리자)
    private boolean isMaster(String username) {
        User user = userService.findByUsername(username);
        return user.getUserRole() == UserRole.MASTER;
    }


}
