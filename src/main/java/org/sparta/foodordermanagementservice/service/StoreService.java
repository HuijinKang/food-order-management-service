package org.sparta.foodordermanagementservice.service;

import org.sparta.foodordermanagementservice.entity.Store;

import java.util.List;
import java.util.UUID;

public interface StoreService {
    // 지정된 거리(10km) 이내의 가게들만 검색
    public List<Store> getStoresWithinRadius(double latitude, double longitude);
    // 두 지점 간의 거리를 계산
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2);
    // 가게 단건 조회
    public Store getStoreById(UUID storeId);
}
