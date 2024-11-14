package org.sparta.foodordermanagementservice.service;

import org.sparta.foodordermanagementservice.entity.Category;

import java.util.UUID;

public interface CategoryService {

    // 카테고리 조회
    Category getCategoryById(UUID categoryId);
}
