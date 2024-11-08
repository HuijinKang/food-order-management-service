package org.sparta.foodordermanagementservice.store.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.sparta.foodordermanagementservice.entity.Store;
import org.sparta.foodordermanagementservice.repository.StoreRepository;
import org.sparta.foodordermanagementservice.service.StoreService;
import org.sparta.foodordermanagementservice.service.impl.StoreServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class StoreServiceTest {

    private StoreService storeService;
    private StoreRepository storeRepository;

    @BeforeEach
    void setUp() {
        // Mock 생성
        storeRepository = Mockito.mock(StoreRepository.class);
        storeService = new StoreServiceImpl(storeRepository);

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
                .latitude(latitude)
                .longitude(longitude)
                .name(name)
                .createdAt(LocalDateTime.now())
                .createdBy("test")
                .updatedAt(LocalDateTime.now())
                .updatedBy("test")
                .build();
    }
}
