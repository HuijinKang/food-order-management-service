package org.sparta.foodordermanagementservice.service.Impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sparta.foodordermanagementservice.dto.request.CategoryRegistrationRequestDTO;
import org.sparta.foodordermanagementservice.dto.request.CategoryUpdateRequestDTO;
import org.sparta.foodordermanagementservice.dto.response.CategoryStoreListDTO;
import org.sparta.foodordermanagementservice.entity.*;
import org.sparta.foodordermanagementservice.repository.CategoryRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("getCategoryById - 성공")
    public void testGetCategoryById_Success() {
        UUID categoryId = UUID.randomUUID();
        Category category = new Category();
        category.setId(categoryId);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        Category result = categoryService.getCategoryById(categoryId);

        assertEquals(category, result);
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    @DisplayName("getCategoryById - 실패 (카테고리 없음)")
    public void testGetCategoryById_NotFound() {
        UUID categoryId = UUID.randomUUID();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            categoryService.getCategoryById(categoryId);
        });

        assertTrue(exception.getMessage().contains("Category not found with id: " + categoryId));
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    @DisplayName("getStoresByCategory - 성공")
    public void testGetStoresByCategory_Success() {
        String categoryName = "음식";
        Category category = new Category();
        category.setName(categoryName);

        Store store1 = new Store();
        store1.setDeletedAt(null);
        Store store2 = new Store();
        store2.setDeletedAt(LocalDateTime.now()); // 삭제된 가게
        Store store3 = new Store();
        store3.setDeletedAt(null);

        Set<Store> stores = new HashSet<>(Arrays.asList(store1, store2, store3));
        category.setStores(stores);

        when(categoryRepository.findByNameIgnoreCase(categoryName)).thenReturn(Optional.of(category));

        List<CategoryStoreListDTO> result = categoryService.getStoresByCategory(categoryName);

        assertEquals(2, result.size());
        verify(categoryRepository, times(1)).findByNameIgnoreCase(categoryName);
    }

    @Test
    @DisplayName("getStoresByCategory - 실패 (카테고리 없음)")
    public void testGetStoresByCategory_CategoryNotFound() {
        String categoryName = "없는카테고리";

        when(categoryRepository.findByNameIgnoreCase(categoryName)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            categoryService.getStoresByCategory(categoryName);
        });

        assertEquals("Category not found: " + categoryName, exception.getMessage());
        verify(categoryRepository, times(1)).findByNameIgnoreCase(categoryName);
    }

    @Test
    @DisplayName("registerCategory - 성공")
    public void testRegisterCategory_Success() {
        String categoryName = "새로운카테고리";
        User user = User.builder().username("testuser").build();

        CategoryRegistrationRequestDTO requestDTO = new CategoryRegistrationRequestDTO();
        requestDTO.setName(categoryName);

        when(categoryRepository.existsByName(categoryName)).thenReturn(false);

        Category savedCategory = new Category();
        savedCategory.setId(UUID.randomUUID());
        savedCategory.setName(categoryName);
        savedCategory.setCreatedBy(user.getUsername());
        savedCategory.setUpdatedBy(user.getUsername());

        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        Category result = categoryService.registerCategory(requestDTO, user);

        assertEquals(savedCategory, result);
        verify(categoryRepository, times(1)).existsByName(categoryName);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    @DisplayName("registerCategory - 실패 (이름 중복)")
    public void testRegisterCategory_DuplicateName() {
        String categoryName = "중복카테고리";
        User user = User.builder().username("testuser").build();

        CategoryRegistrationRequestDTO requestDTO = new CategoryRegistrationRequestDTO();
        requestDTO.setName(categoryName);

        when(categoryRepository.existsByName(categoryName)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            categoryService.registerCategory(requestDTO, user);
        });

        assertEquals("Category name already exists: " + categoryName, exception.getMessage());
        verify(categoryRepository, times(1)).existsByName(categoryName);
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    @DisplayName("updateCategory - 성공")
    public void testUpdateCategory_Success() {
        UUID categoryId = UUID.randomUUID();
        String oldName = "기존카테고리";
        String newName = "새로운카테고리";
        User user = User.builder().username("testuser").build();

        CategoryUpdateRequestDTO updateRequestDTO = new CategoryUpdateRequestDTO();
        updateRequestDTO.setName(newName);

        Category category = new Category();
        category.setId(categoryId);
        category.setName(oldName);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryRepository.existsByName(newName)).thenReturn(false);
        when(categoryRepository.save(category)).thenReturn(category);

        Category result = categoryService.updateCategory(categoryId, updateRequestDTO, user);

        assertEquals(newName, result.getName());
        assertEquals(user.getUsername(), result.getUpdatedBy());
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).existsByName(newName);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    @DisplayName("updateCategory - 실패 (이름 중복)")
    public void testUpdateCategory_DuplicateName() {
        UUID categoryId = UUID.randomUUID();
        String oldName = "기존카테고리";
        String duplicateName = "중복카테고리";
        User user = User.builder().username("testuser").build();

        CategoryUpdateRequestDTO updateRequestDTO = new CategoryUpdateRequestDTO();
        updateRequestDTO.setName(duplicateName);

        Category category = new Category();
        category.setId(categoryId);
        category.setName(oldName);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryRepository.existsByName(duplicateName)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            categoryService.updateCategory(categoryId, updateRequestDTO, user);
        });

        assertEquals("Category name already exists: " + duplicateName, exception.getMessage());
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).existsByName(duplicateName);
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    @DisplayName("deleteCategory - 성공")
    public void testDeleteCategory_Success() {
        UUID categoryId = UUID.randomUUID();
        Category category = new Category();
        category.setId(categoryId);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testuser");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);

        Category result = categoryService.deleteCategory(categoryId, userDetails);

        assertEquals(category, result);
        assertNotNull(category.getDeletedAt());
        assertEquals("testuser", category.getDeletedBy());
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    @DisplayName("deleteCategory - 실패 (카테고리 없음)")
    public void testDeleteCategory_CategoryNotFound() {
        UUID categoryId = UUID.randomUUID();

        UserDetails userDetails = mock(UserDetails.class);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            categoryService.deleteCategory(categoryId, userDetails);
        });

        assertTrue(exception.getMessage().contains("Category not found with id: " + categoryId));
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    @DisplayName("checkDuplicateCategoryName - 실패 (이름 존재)")
    public void testCheckDuplicateCategoryName_NameExists() {
        String name = "중복카테고리";

        when(categoryRepository.existsByName(name)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            categoryService.checkDuplicateCategoryName(name);
        });

        assertEquals("Category name already exists: " + name, exception.getMessage());
        verify(categoryRepository, times(1)).existsByName(name);
    }

    @Test
    @DisplayName("checkDuplicateCategoryName - 성공 (이름 없음)")
    public void testCheckDuplicateCategoryName_NameDoesNotExist() {
        String name = "새로운카테고리";

        when(categoryRepository.existsByName(name)).thenReturn(false);

        assertDoesNotThrow(() -> {
            categoryService.checkDuplicateCategoryName(name);
        });

        verify(categoryRepository, times(1)).existsByName(name);
    }
}
