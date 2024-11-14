package org.sparta.foodordermanagementservice.service.Impl;

import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.dto.request.CategoryRegistrationRequestDTO;
import org.sparta.foodordermanagementservice.dto.request.UpdateCategoryRequestDTO;
import org.sparta.foodordermanagementservice.dto.response.CategoryStoreListDTO;
import org.sparta.foodordermanagementservice.entity.Category;
import org.sparta.foodordermanagementservice.entity.Store;
import org.sparta.foodordermanagementservice.entity.User;
import org.sparta.foodordermanagementservice.repository.CategoryRepository;
import org.sparta.foodordermanagementservice.security.UserDetailsImpl;
import org.sparta.foodordermanagementservice.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Category getCategoryById(UUID categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found with id: " + categoryId));

    }

    @Transactional(readOnly = true)
    public List<CategoryStoreListDTO> getStoresByCategory(String categoryName) {
        // 카테고리 조회
        Category category = categoryRepository.findByNameIgnoreCase(categoryName)
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + categoryName));

        // 해당 카테고리에 속하는 가게 목록을 DTO로 변환
        return category.getStores().stream()
                .filter(store -> store.getDeletedAt() == null) // 삭제되지 않은 가게만 반환
                .map(this::toStoreResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Category registerCategory(CategoryRegistrationRequestDTO registrationRequestDTO, User user) {
        // 카테고리 이름 중복 검사
        checkDuplicateCategoryName(registrationRequestDTO.getName());

        Category category = Category.builder()
                .name(registrationRequestDTO.getName())
                .createdAt(LocalDateTime.now())
                .createdBy(user.getUsername())
                .updatedAt(LocalDateTime.now())
                .updatedBy(user.getUsername())
                .build();

        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(UUID categoryId, UpdateCategoryRequestDTO updateCategoryRequestDTO, User user) {
        Category category = getCategoryById(categoryId);

        // 이름 업데이트 요청이 있는 경우 중복 체크 및 업데이트
        if (updateCategoryRequestDTO.getName() != null && !updateCategoryRequestDTO.getName().equals(category.getName())) {
            checkDuplicateCategoryName(updateCategoryRequestDTO.getName());
            category.setName(updateCategoryRequestDTO.getName());
            category.setUpdatedAt(LocalDateTime.now());
            category.setUpdatedBy(user.getUsername());
        }

        return categoryRepository.save(category);
    }

    @Override
    public Category deleteCategory(UUID categoryId, UserDetailsImpl userDetails) {
        Category category = getCategoryById(categoryId);

        category.setDeletedAt(LocalDateTime.now());
        category.setDeletedBy(userDetails.getUsername());

        return categoryRepository.save(category);
    }

    @Override
    public void checkDuplicateCategoryName(String name) {
        boolean nameExists = categoryRepository.existsByName(name);
        if (nameExists) {
            throw new IllegalArgumentException("Category name already exists: " + name);
        }
    }

    @Override
    public CategoryStoreListDTO toStoreResponseDTO(Store store) {
        return CategoryStoreListDTO.builder()
                .id(store.getId())
                .name(store.getName())
                .region(store.getRegion())
                .latitude(store.getLatitude())
                .longitude(store.getLongitude())
                .createdAt(store.getCreatedAt())
                .createdBy(store.getCreatedBy())
                .updatedAt(store.getUpdatedAt())
                .updatedBy(store.getUpdatedBy())
                .build();
    }
}
