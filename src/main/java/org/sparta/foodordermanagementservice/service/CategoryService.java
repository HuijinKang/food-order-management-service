package org.sparta.foodordermanagementservice.service;

import org.sparta.foodordermanagementservice.dto.request.CategoryRegistrationRequestDTO;
import org.sparta.foodordermanagementservice.dto.request.UpdateCategoryRequestDTO;
import org.sparta.foodordermanagementservice.dto.response.CategoryStoreListDTO;
import org.sparta.foodordermanagementservice.entity.Category;
import org.sparta.foodordermanagementservice.entity.Store;
import org.sparta.foodordermanagementservice.entity.User;
import org.sparta.foodordermanagementservice.security.UserDetailsImpl;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    // 카테고리 조회
    Category getCategoryById(UUID categoryId);

    // 카테고리명으로 가게 조회
    List<CategoryStoreListDTO> getStoresByCategory(String categoryName);

    // 카테고리 등록
    Category registerCategory(CategoryRegistrationRequestDTO registrationRequestDTO, User user);

    // 카테고리 수정
    Category updateCategory(UUID categoryId, UpdateCategoryRequestDTO updateCategoryRequestDTO, User user);

    // 카테고리 삭제
    Category deleteCategory(UUID categoryId, UserDetailsImpl userDetails);

    // 동일한 카테고리명이 있을시 예외 발생
    void checkDuplicateCategoryName(String name);

    // Entity -> DTO
    CategoryStoreListDTO toStoreResponseDTO(Store store);
}
