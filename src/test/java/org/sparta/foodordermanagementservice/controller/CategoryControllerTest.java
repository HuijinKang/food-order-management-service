package org.sparta.foodordermanagementservice.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.sparta.foodordermanagementservice.entity.Category;
import org.sparta.foodordermanagementservice.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Test
    public void getCategoryById_Success() throws Exception {
        UUID categoryId = UUID.randomUUID();
        Category category = Category.builder()
                .id(categoryId)
                .name("Sample Category")
                .createdAt(LocalDateTime.now())
                .createdBy("Admin")
                .updatedAt(LocalDateTime.now())
                .updatedBy("Admin")
                .build();

        Mockito.when(categoryService.getCategoryById(categoryId)).thenReturn(category);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/category/" + categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(categoryId.toString()))
                .andExpect(jsonPath("$.data.name").value("Sample Category"));
    }
}

