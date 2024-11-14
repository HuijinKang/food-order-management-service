package org.sparta.foodordermanagementservice.service.Impl;

import lombok.RequiredArgsConstructor;
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
}
