package org.sparta.foodordermanagementservice.controller;

import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.common.ApiResponse;
import org.sparta.foodordermanagementservice.dto.request.CategoryRegistrationRequestDTO;
import org.sparta.foodordermanagementservice.dto.request.UpdateCategoryRequestDTO;
import org.sparta.foodordermanagementservice.dto.response.CategoryStoreListDTO;
import org.sparta.foodordermanagementservice.dto.response.StoreSearchResponseDTO;
import org.sparta.foodordermanagementservice.entity.Category;
import org.sparta.foodordermanagementservice.entity.UserRole;
import org.sparta.foodordermanagementservice.security.UserDetailsImpl;
import org.sparta.foodordermanagementservice.service.CategoryService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryService categoryService;

    // 카테고리 조회
    @GetMapping("/{categoryId}")
    @Secured({UserRole.Authority.MASTER})
    public ApiResponse<Category> getCategoryById(@PathVariable UUID categoryId) {
        return ApiResponse.ofSuccess(categoryService.getCategoryById(categoryId));
    }

    // 카테고리명으로 가게 조회
    @GetMapping("/{categoryName}/stores")
    public ApiResponse<List<CategoryStoreListDTO>> getStoresByCategory(@PathVariable String categoryName) {
        List<CategoryStoreListDTO> stores = categoryService.getStoresByCategory(categoryName);
        return ApiResponse.ofSuccess(stores);
    }

    // 카테고리 등록
    @PostMapping
    @Secured({UserRole.Authority.MASTER})
    public ApiResponse<Category> registerCategory(@RequestBody CategoryRegistrationRequestDTO registrationRequestDTO,
                                                  @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ApiResponse.ofSuccess(categoryService.registerCategory(registrationRequestDTO, userDetails.getUser()));
    }

    // 카테고리 수정
    @PatchMapping("/{categoryId}")
    @Secured({UserRole.Authority.MASTER})
    public ApiResponse<Category> updateCategory(@PathVariable UUID categoryId,
                                                @RequestBody UpdateCategoryRequestDTO updateCategoryRequestDTO,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ApiResponse.ofSuccess(categoryService.updateCategory(categoryId, updateCategoryRequestDTO, userDetails.getUser()));
    }

    // 카테고리 삭제
    @DeleteMapping("/{categoryId}")
    @Secured({UserRole.Authority.MASTER})
    public ApiResponse<Category> deleteCategory(@PathVariable UUID categoryId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ApiResponse.ofSuccess(categoryService.deleteCategory(categoryId, userDetails));
    }
}
