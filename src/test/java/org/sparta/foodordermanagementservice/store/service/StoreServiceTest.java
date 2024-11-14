package org.sparta.foodordermanagementservice.store.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sparta.foodordermanagementservice.dto.request.StoreRegistrationRequestDTO;
import org.sparta.foodordermanagementservice.entity.Category;
import org.sparta.foodordermanagementservice.entity.Store;
import org.sparta.foodordermanagementservice.entity.User;
import org.sparta.foodordermanagementservice.repository.CategoryRepository;
import org.sparta.foodordermanagementservice.repository.StoreRepository;
import org.sparta.foodordermanagementservice.service.Impl.StoreServiceImpl;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private StoreServiceImpl storeService;

    private List<Store> mockStores;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockStores = createMockStores();
        when(storeRepository.findAll()).thenReturn(mockStores);
    }

    @Test
    void testRegisterStore() {
        UUID categoryId1 = UUID.randomUUID();
        UUID categoryId2 = UUID.randomUUID();

        StoreRegistrationRequestDTO requestDto = StoreRegistrationRequestDTO.builder()
                .region("Seoul")
                .latitude(37.5665)
                .longitude(126.9780)
                .name("Pizza Hut")
                .category(List.of(categoryId1, categoryId2))
                .build();

        Category category1 = createCategory(categoryId1, "양식");
        Category category2 = createCategory(categoryId2, "햄버거");

        when(categoryRepository.findById(categoryId1)).thenReturn(Optional.of(category1));
        when(categoryRepository.findById(categoryId2)).thenReturn(Optional.of(category2));
        when(storeRepository.save(any(Store.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Store registeredStore = storeService.registerStore(requestDto, new User());

        assertEquals("Seoul", registeredStore.getRegion());
        assertEquals(37.5665, registeredStore.getLatitude());
        assertEquals(126.9780, registeredStore.getLongitude());
        assertEquals("Pizza Hut", registeredStore.getName());
        assertEquals(Set.of(category1, category2), registeredStore.getCategories());

        verify(categoryRepository, times(1)).findById(categoryId1);
        verify(categoryRepository, times(1)).findById(categoryId2);
        verify(storeRepository, times(1)).save(any(Store.class));
    }

    private List<Store> createMockStores() {
        return List.of(
                createStore("Store A", 37.5700, 126.9800),
                createStore("Store B", 37.5750, 126.9850),
                createStore("Store C", 37.5765, 126.9700),
                createStore("Store D", 37.6340, 127.0907),
                createStore("Store E", 37.6600, 127.0800)
        );
    }

    private Store createStore(String name, double latitude, double longitude) {
        return createStore(name, latitude, longitude, new HashSet<>());
    }

    private Store createStore(String name, double latitude, double longitude, Set<Category> categories) {
        return Store.builder()
                .id(UUID.randomUUID())
                .name(name)
                .latitude(latitude)
                .longitude(longitude)
                .region("Test Region")
                .categories(categories)
                .totalRating(5)
                .reviewCount(10)
                .createdAt(LocalDateTime.now())
                .createdBy("test")
                .updatedAt(LocalDateTime.now())
                .updatedBy("test")
                .build();
    }

    private Category createCategory(UUID id, String name) {
        return Category.builder()
                .id(id)
                .name(name)
                .createdAt(LocalDateTime.now())
                .createdBy("test")
                .updatedAt(LocalDateTime.now())
                .updatedBy("test")
                .build();
    }
}
