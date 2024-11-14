package org.sparta.foodordermanagementservice.controller;

import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.common.ApiResponse;
import org.sparta.foodordermanagementservice.entity.Category;
import org.sparta.foodordermanagementservice.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
