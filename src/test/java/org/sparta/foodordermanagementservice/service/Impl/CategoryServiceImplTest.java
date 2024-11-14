package org.sparta.foodordermanagementservice.service.Impl;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.sparta.foodordermanagementservice.dto.request.UpdateCategoryRequestDTO;
import org.sparta.foodordermanagementservice.entity.Category;
import org.sparta.foodordermanagementservice.repository.CategoryRepository;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    public void getCategoryById_Success() {
        UUID categoryId = UUID.randomUUID();
        Category category = Category.builder()
                .id(categoryId)
                .name("Sample Category")
                .createdAt(LocalDateTime.now())
                .createdBy("admin")
                .updatedAt(LocalDateTime.now())
                .updatedBy("admin")
                .build();

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        Category foundCategory = categoryService.getCategoryById(categoryId);

        assertNotNull(foundCategory);
        assertEquals(category.getId(), foundCategory.getId());
        assertEquals(category.getName(), foundCategory.getName());
    }

//    @Test
//    public void getCategoryById_NotFound() {
//        UUID categoryId = UUID.randomUUID();
//
//        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
//
//        assertThrows(IllegalArgumentException.class, () -> categoryService.getCategoryById(categoryId));
//    }

    @Test
    public void updateCategory_Success() {
        UUID categoryId = UUID.randomUUID();
        Category existingCategory = Category.builder()
                .id(categoryId)
                .name("Old Category Name")
                .createdAt(LocalDateTime.now())
                .createdBy("admin")
                .updatedAt(LocalDateTime.now())
                .updatedBy("admin")
                .build();

        UpdateCategoryRequestDTO updateRequest = new UpdateCategoryRequestDTO();
        updateRequest.setName("New Category Name");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.existsByName(updateRequest.getName())).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(existingCategory);

        Category updatedCategory = categoryService.updateCategory(categoryId, updateRequest);

        assertNotNull(updatedCategory);
        assertEquals("New Category Name", updatedCategory.getName());
        verify(categoryRepository, times(1)).save(existingCategory);
    }

    @Test
    public void updateCategory_NameAlreadyExists() {
        UUID categoryId = UUID.randomUUID();
        Category existingCategory = Category.builder()
                .id(categoryId)
                .name("Old Category Name")
                .createdAt(LocalDateTime.now())
                .createdBy("admin")
                .updatedAt(LocalDateTime.now())
                .updatedBy("admin")
                .build();

        UpdateCategoryRequestDTO updateRequest = new UpdateCategoryRequestDTO();
        updateRequest.setName("Duplicate Category Name");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.existsByName(updateRequest.getName())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> categoryService.updateCategory(categoryId, updateRequest));

        verify(categoryRepository, never()).save(existingCategory);
    }

    @Test
    public void updateCategory_CategoryNotFound() {
        UUID categoryId = UUID.randomUUID();
        UpdateCategoryRequestDTO updateRequest = new UpdateCategoryRequestDTO();
        updateRequest.setName("New Category Name");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> categoryService.updateCategory(categoryId, updateRequest));

        verify(categoryRepository, never()).save(any(Category.class));
    }
}
