package org.sparta.foodordermanagementservice.service.Impl;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.sparta.foodordermanagementservice.dto.request.CategoryRegistrationRequestDTO;
import org.sparta.foodordermanagementservice.dto.request.CategoryUpdateRequestDTO;
import org.sparta.foodordermanagementservice.entity.Category;
import org.sparta.foodordermanagementservice.entity.User;
import org.sparta.foodordermanagementservice.repository.CategoryRepository;
import org.sparta.foodordermanagementservice.security.UserDetailsImpl;
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

    @Mock
    private UserDetailsImpl userDetails;

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
    public void registerCategory_Success() {
        // Given
        CategoryRegistrationRequestDTO registrationRequest = new CategoryRegistrationRequestDTO();
        registrationRequest.setName("New Category");

        Category savedCategory = Category.builder()
                .id(UUID.randomUUID())
                .name("New Category")
                .createdAt(LocalDateTime.now())
                .createdBy("admin")
                .updatedAt(LocalDateTime.now())
                .updatedBy("admin")
                .build();

        // When
        when(categoryRepository.existsByName(registrationRequest.getName())).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        Category result = categoryService.registerCategory(registrationRequest, new User());

        // Then
        assertNotNull(result);
        assertEquals("New Category", result.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

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

        CategoryUpdateRequestDTO updateRequest = new CategoryUpdateRequestDTO();
        updateRequest.setName("New Category Name");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.existsByName(updateRequest.getName())).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(existingCategory);

        Category updatedCategory = categoryService.updateCategory(categoryId, updateRequest, new User());

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

        CategoryUpdateRequestDTO updateRequest = new CategoryUpdateRequestDTO();
        updateRequest.setName("Duplicate Category Name");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.existsByName(updateRequest.getName())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> categoryService.updateCategory(categoryId, updateRequest, new User()));

        verify(categoryRepository, never()).save(existingCategory);
    }

    @Test
    public void updateCategory_CategoryNotFound() {
        UUID categoryId = UUID.randomUUID();
        CategoryUpdateRequestDTO updateRequest = new CategoryUpdateRequestDTO();
        updateRequest.setName("New Category Name");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> categoryService.updateCategory(categoryId, updateRequest, new User()));

        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    public void deleteCategory_Success() {
        // Given
        UUID categoryId = UUID.randomUUID();
        Category category = Category.builder()
                .id(categoryId)
                .name("Sample Category")
                .createdAt(LocalDateTime.now())
                .createdBy("admin")
                .updatedAt(LocalDateTime.now())
                .updatedBy("admin")
                .build();

        // UserDetails 모킹 설정
        when(userDetails.getUsername()).thenReturn("testUser");

        // CategoryRepository 모킹 설정
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        // When
        Category deletedCategory = categoryService.deleteCategory(categoryId, userDetails);

        // Then
        assertNotNull(deletedCategory.getDeletedAt());
        assertEquals("testUser", deletedCategory.getDeletedBy());
        verify(categoryRepository, times(1)).save(category);
    }

}
