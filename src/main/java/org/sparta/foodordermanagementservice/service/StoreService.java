package org.sparta.foodordermanagementservice.service;

import org.sparta.foodordermanagementservice.dto.request.StoreRegistrationRequestDTO;
import org.sparta.foodordermanagementservice.dto.request.StoreUpdateRequestDTO;
import org.sparta.foodordermanagementservice.dto.response.StoreSearchResponseDTO;
import org.sparta.foodordermanagementservice.dto.response.StoreUpdateResponseDTO;
import org.sparta.foodordermanagementservice.entity.Category;
import org.sparta.foodordermanagementservice.entity.Store;
import org.sparta.foodordermanagementservice.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface StoreService {
    // 지정된 거리(10km) 이내의 가게들만 검색
    public List<StoreSearchResponseDTO> getStoresWithinRadius(double latitude, double longitude);

    // 두 지점 간의 거리를 계산
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2);

    // 가게 단건 조회
    public Store getStoreById(UUID storeId);

    // 가게 검색
    public Page<Store> getSearchStoreList(String keyword, int pageSize, int pageNumber, String sortBy, boolean isAsc);

    // 가게 등록
    public Store registerStore(StoreRegistrationRequestDTO storeRegistrationRequestDTO, User user);

    // 가게 정보 수정
    public StoreUpdateResponseDTO updateStore(UUID storeId, StoreUpdateRequestDTO storeUpdateRequestDTO, User user);

    // 가게 삭제
    public void deleteStore(UUID storeId);

    // 가게가 존재하는 지 체크
    public void checkStoreExists(UUID storeId);

    // 가게 이름 중복 체크
    public void checkStoreNameDuplication(String storeName);

    // 카테고리 중복 체크
    public Set<Category> getCategoriesByIds(List<UUID> categoryIds);

    // Entity -> DTO
    public StoreSearchResponseDTO toStoreSearchResponseDTO(Store store);

    // Entity -> DTO
    public StoreUpdateResponseDTO toStoreUpdateResponseDTO(Store store);
}