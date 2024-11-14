package org.sparta.foodordermanagementservice.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.sparta.foodordermanagementservice.dto.response.CategoryStoreListDTO;
import org.sparta.foodordermanagementservice.entity.Category;
import org.sparta.foodordermanagementservice.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

        mockMvc.perform(get("/api/category/" + categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(categoryId.toString()))
                .andExpect(jsonPath("$.data.name").value("Sample Category"));
    }

    @Test
    public void getStoresByCategory_ShouldReturnStoreList() throws Exception {
        List<CategoryStoreListDTO> stores = Arrays.asList(
                CategoryStoreListDTO.builder()
                        .id(UUID.randomUUID())
                        .name("Store A")
                        .region("Seoul")
                        .latitude(37.5665)
                        .longitude(126.9780)
                        .updatedAt(null)
                        .updatedBy("user1")
                        .build(),
                CategoryStoreListDTO.builder()
                        .id(UUID.randomUUID())
                        .name("Store B")
                        .region("Busan")
                        .latitude(35.1796)
                        .longitude(129.0756)
                        .updatedAt(null)
                        .updatedBy("user2")
                        .build()
        );

        Mockito.when(categoryService.getStoresByCategory(anyString())).thenReturn(stores);

        mockMvc.perform(get("/api/category/testCategory/stores")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("Store A"))
                .andExpect(jsonPath("$.data[1].name").value("Store B"));
    }
}

