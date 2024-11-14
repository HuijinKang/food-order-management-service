package org.sparta.foodordermanagementservice.store.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sparta.foodordermanagementservice.dto.request.StoreRegistrationRequestDTO;
import org.sparta.foodordermanagementservice.dto.response.StoreSearchResponseDTO;
import org.sparta.foodordermanagementservice.entity.Category;
import org.sparta.foodordermanagementservice.entity.Store;
import org.sparta.foodordermanagementservice.entity.User;
import org.sparta.foodordermanagementservice.repository.CategoryRepository;
import org.sparta.foodordermanagementservice.repository.StoreRepository;
import org.sparta.foodordermanagementservice.service.Impl.StoreServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
    void testGetSearchStoreList() {
        Category italianCategory = createCategory("Italian");
        Store store1 = createStore("Italian Pizza Place", 37.5700, 126.9800, Set.of(italianCategory));
        Store store2 = createStore("Pasta House", 37.5750, 126.9850, Set.of(italianCategory));

        List<Store> stores = List.of(store1, store2);
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Store> storePage = new PageImpl<>(stores, pageRequest, stores.size());

        when(storeRepository.searchStores(anyString(), anyString(), any(PageRequest.class))).thenReturn(storePage);

        Page<Store> result = storeService.getSearchStoreList("Italian", 10, 0, "createdAt", false);

        assertEquals(2, result.getTotalElements());
        assertEquals("Italian Pizza Place", result.getContent().get(0).getName());
        assertEquals("Pasta House", result.getContent().get(1).getName());
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

    @Test
    void testGetStoresWithinRadius() {
        double clientLatitude = 37.5665;
        double clientLongitude = 126.9780;

        List<StoreSearchResponseDTO> result = storeService.getStoresWithinRadius(clientLatitude, clientLongitude);

        assertEquals(3, result.size());
        assertEquals("Store A", result.get(0).getName());
        assertEquals("Store B", result.get(1).getName());
        assertEquals("Store C", result.get(2).getName());
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

    private Category createCategory(String name) {
        return Category.builder()
                .id(UUID.randomUUID())
                .name(name)
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
