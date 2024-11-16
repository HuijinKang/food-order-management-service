package org.sparta.foodordermanagementservice.service;

import org.sparta.foodordermanagementservice.dto.request.SearchRequestDto;
import org.sparta.foodordermanagementservice.dto.request.StoreRegistrationRequestDTO;
import org.sparta.foodordermanagementservice.dto.request.StoreUpdateRequestDTO;
import org.sparta.foodordermanagementservice.dto.response.StoreUpdateResponseDTO;
import org.sparta.foodordermanagementservice.entity.Category;
import org.sparta.foodordermanagementservice.entity.Store;
import org.sparta.foodordermanagementservice.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface StoreService {
    // 가게 단건 조회
    Store getStoreById(UUID storeId);

    // 가게 검색
    Page<Store> getSearchStoreList(double latitude, double longitude, SearchRequestDto searchRequestDto);

    // 가게 등록
    Store registerStore(StoreRegistrationRequestDTO storeRegistrationRequestDTO, User user);

    // 가게 정보 수정
    StoreUpdateResponseDTO updateStore(UUID storeId, StoreUpdateRequestDTO storeUpdateRequestDTO, User user);

    // 가게 삭제
    Store deleteStore(UUID storeId, User user);

    // 가게가 존재하는 지 체크
    void checkStoreExists(UUID storeId);

    // 가게 이름 중복 체크
    void checkStoreNameDuplication(String storeName);

    // 카테고리 중복 체크 및 사용 가능 여부 검증
    Set<Category> getCategoriesByIds(List<UUID> categoryIds);

    // Entity -> DTO
    StoreUpdateResponseDTO toStoreUpdateResponseDTO(Store store);

    Store findByStoreId(UUID storeId);
}