//package org.sparta.foodordermanagementservice.repository;
//
//import org.junit.jupiter.api.Test;
//import org.sparta.foodordermanagementservice.entity.Category;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//public class CategoryRepositoryTest {
//
//    @Autowired
//    private CategoryRepository categoryRepository;
//
//    @Test
//    public void testFindById_Success() {
//        Category category = Category.builder()
//                .name("Test Category")
//                .build();
//
//        categoryRepository.save(category);
//        Optional<Category> foundCategory = categoryRepository.findById(category.getId());
//
//        assertTrue(foundCategory.isPresent());
//        assertEquals(category.getId(), foundCategory.get().getId());
//    }
//
//    @Test
//    public void testFindById_NotFound() {
//        UUID categoryId = UUID.randomUUID();
//
//        Optional<Category> foundCategory = categoryRepository.findById(categoryId);
//
//        assertFalse(foundCategory.isPresent());
//    }
//}
//
