package org.sparta.foodordermanagementservice.service.Impl;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.sparta.foodordermanagementservice.entity.Category;
import org.sparta.foodordermanagementservice.repository.CategoryRepository;
import org.sparta.foodordermanagementservice.service.CategoryService;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    public void getCategoryById_NotFound() {
        UUID categoryId = UUID.randomUUID();

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> categoryService.getCategoryById(categoryId));
    }
}
