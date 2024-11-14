package org.sparta.foodordermanagementservice.service;

import org.sparta.foodordermanagementservice.dto.request.CategoryRegistrationRequestDTO;
import org.sparta.foodordermanagementservice.dto.request.UpdateCategoryRequestDTO;
import org.sparta.foodordermanagementservice.entity.Category;
import org.sparta.foodordermanagementservice.entity.User;

import java.util.UUID;

public interface CategoryService {
    // 카테고리 조회
    Category getCategoryById(UUID categoryId);

    // 카테고리 등록
    Category registerCategory(CategoryRegistrationRequestDTO registrationRequestDTO, User user);

    // 카테고리 수정
    Category updateCategory(UUID categoryId, UpdateCategoryRequestDTO updateCategoryRequestDTO, User user);

    // 동일한 카테고리명이 있을시 예외 발생
    void checkDuplicateCategoryName(String name);
}
