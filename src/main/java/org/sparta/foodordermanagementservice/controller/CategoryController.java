package org.sparta.foodordermanagementservice.controller;

import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.common.ApiResponse;
import org.sparta.foodordermanagementservice.dto.request.UpdateCategoryRequestDTO;
import org.sparta.foodordermanagementservice.entity.Category;
import org.sparta.foodordermanagementservice.entity.UserRole;
import org.sparta.foodordermanagementservice.service.CategoryService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryService categoryService;

    // 카테고리 조회
    @GetMapping("/{categoryId}")
    public ApiResponse<Category> getCategoryById(@PathVariable UUID categoryId) {
        return ApiResponse.ofSuccess(categoryService.getCategoryById(categoryId));
    }

    // 카테고리 수정
    @PatchMapping("/{categoryId}")
    @Secured({UserRole.Authority.MASTER})
    public ApiResponse<Category> updateCategory(@PathVariable UUID categoryId,
                                                @RequestBody UpdateCategoryRequestDTO updateCategoryRequestDTO) {
        return ApiResponse.ofSuccess(categoryService.updateCategory(categoryId, updateCategoryRequestDTO));
    }
}
