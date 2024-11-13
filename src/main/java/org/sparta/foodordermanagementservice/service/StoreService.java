package org.sparta.foodordermanagementservice.service;

import org.sparta.foodordermanagementservice.dto.request.SortedBy;
import org.sparta.foodordermanagementservice.dto.request.StoreRequest;
import org.sparta.foodordermanagementservice.entity.Store;
import org.sparta.foodordermanagementservice.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface StoreService {
    // 지정된 거리(10km) 이내의 가게들만 검색
    public List<Store> getStoresWithinRadius(double latitude, double longitude);

    // 두 지점 간의 거리를 계산
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2);

    // 가게 단건 조회
    public Store getStoreById(UUID storeId);

    // 가게 검색
    public Page<Store> getSearchStoreList(String keyword, int pageSize, int pageNumber, String sortBy, boolean isAsc);

    // 가게 등록
    public Store registerStore(StoreRequest storeRequest, User user);

    // 가게 정보 수정
    public Store updateStore(UUID storeId, StoreRequest storeRequest, User user);

    // 가게 삭제
    public void deleteStore(UUID storeId);
}