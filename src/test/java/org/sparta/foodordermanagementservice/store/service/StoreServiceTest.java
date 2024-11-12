package org.sparta.foodordermanagementservice.store.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sparta.foodordermanagementservice.dto.request.StoreRequest;
import org.sparta.foodordermanagementservice.entity.Category;
import org.sparta.foodordermanagementservice.entity.Store;
import org.sparta.foodordermanagementservice.entity.User;
import org.sparta.foodordermanagementservice.repository.CategoryRepository;
import org.sparta.foodordermanagementservice.repository.StoreRepository;
import org.sparta.foodordermanagementservice.service.StoreServiceImpl;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        List<Store> mockStores = new ArrayList<>();

        // 10km 이내의 가게들
        mockStores.add(createStore("Store A", 37.5700, 126.9800)); // 약 0.5km 거리
        mockStores.add(createStore("Store B", 37.5750, 126.9850)); // 약 1km 거리
        mockStores.add(createStore("Store C", 37.5765, 126.9700)); // 약 2km 거리

        // 10km 밖의 가게들
        mockStores.add(createStore("Store D", 37.6340, 127.0907)); // 약 15km 거리
        mockStores.add(createStore("Store E", 37.6600, 127.0800)); // 약 20km 거리

        // storeRepository의 findAll 메서드가 mockStores를 반환
        when(storeRepository.findAll()).thenReturn(mockStores);
    }

    // test 목표: 가게 등록 메서드 테스트
    @Test
    void testRegisterStore() {
        // 테스트용 카테고리 ID 생성
        UUID categoryId1 = UUID.randomUUID();
        UUID categoryId2 = UUID.randomUUID();

        StoreRequest requestDto = new StoreRequest();
        requestDto.setRegion("Seoul");
        requestDto.setLatitude(37.5665);
        requestDto.setLongitude(126.9780);
        requestDto.setName("Pizza Hut");
        requestDto.setCategory(List.of(categoryId1, categoryId2));

        // Mock 카테고리 생성 및 설정
        Category category1 = new Category();
        category1.setId(categoryId1);
        category1.setName("양식");

        Category category2 = new Category();
        category2.setId(categoryId2);
        category2.setName("햄버거");

        when(categoryRepository.findById(categoryId1)).thenReturn(Optional.of(category1));
        when(categoryRepository.findById(categoryId2)).thenReturn(Optional.of(category2));

        when(storeRepository.save(any(Store.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 테스트 실행
        Store registeredStore = storeService.registerStore(requestDto, new User());

        // 저장된 가게 정보 검증
        assertEquals("Seoul", registeredStore.getRegion());
        assertEquals(37.5665, registeredStore.getLatitude());
        assertEquals(126.9780, registeredStore.getLongitude());
        assertEquals("Pizza Hut", registeredStore.getName());

        // 카테고리 검증
        Set<Category> categories = registeredStore.getCategories();
        assertEquals(2, categories.size());
        assertEquals(Set.of(category1, category2), categories);

        verify(categoryRepository, times(1)).findById(categoryId1);
        verify(categoryRepository, times(1)).findById(categoryId2);
        verify(storeRepository, times(1)).save(any(Store.class));
    }


    // test 목표: 내 위치에서 10km 이내의 가게들만 나오게.
    @Test
    void testGetStoresWithinRadius() {
        // 클라이언트 위치 설정 (서울 시청)
        double clientLatitude = 37.5665;
        double clientLongitude = 126.9780;

        // 10km 이내의 가게만 반환되는지 테스트
        List<Store> result = storeService.getStoresWithinRadius(clientLatitude, clientLongitude);

        // 예상 결과: 10km 이내 가게가 3개
        assertEquals(3, result.size());
        assertEquals("Store A", result.get(0).getName());
        assertEquals("Store B", result.get(1).getName());
        assertEquals("Store C", result.get(2).getName());
    }

    // 테스트용 가게 객체 생성
    private Store createStore(String name, double latitude, double longitude) {
        return Store.builder()
                .id(UUID.randomUUID())
                .user(null)
                .region("Seoul")
                .latitude(latitude)
                .longitude(longitude)
                .name(name)
                .totalRating(5)
                .reviewCount(10)
                .createdAt(LocalDateTime.now())
                .createdBy("test")
                .updatedAt(LocalDateTime.now())
                .updatedBy("test")
                .build();
    }
}
