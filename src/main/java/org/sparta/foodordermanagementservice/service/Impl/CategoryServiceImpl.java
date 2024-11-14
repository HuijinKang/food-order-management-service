package org.sparta.foodordermanagementservice.service.Impl;

import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.dto.request.UpdateCategoryRequestDTO;
import org.sparta.foodordermanagementservice.entity.Category;
import org.sparta.foodordermanagementservice.repository.CategoryRepository;
import org.sparta.foodordermanagementservice.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Category getCategoryById(UUID categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found with id: " + categoryId));

    }

    @Override
    public Category updateCategory(UUID categoryId, UpdateCategoryRequestDTO updateCategoryRequestDTO) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + categoryId));

        // 이름 업데이트 요청이 있는 경우 중복 체크 및 업데이트
        if (updateCategoryRequestDTO.getName() != null && !updateCategoryRequestDTO.getName().equals(category.getName())) {
            checkDuplicateCategoryName(updateCategoryRequestDTO.getName());
            category.setName(updateCategoryRequestDTO.getName());
        }

        return categoryRepository.save(category);
    }

    @Override
    public void checkDuplicateCategoryName(String name) {
        boolean nameExists = categoryRepository.existsByName(name);
        if (nameExists) {
            throw new IllegalArgumentException("Category name already exists: " + name);
        }
    }
}
