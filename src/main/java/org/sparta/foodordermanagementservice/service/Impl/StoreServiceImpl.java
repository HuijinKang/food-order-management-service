package org.sparta.foodordermanagementservice.service.Impl;

import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.common.exeption.CustomException;
import org.sparta.foodordermanagementservice.common.exeption.ErrorCode;
import org.sparta.foodordermanagementservice.dto.request.StoreRegistrationRequestDTO;
import org.sparta.foodordermanagementservice.dto.request.StoreUpdateRequestDTO;
import org.sparta.foodordermanagementservice.dto.response.StoreUpdateResponseDTO;
import org.sparta.foodordermanagementservice.entity.Category;
import org.sparta.foodordermanagementservice.entity.Store;
import org.sparta.foodordermanagementservice.entity.User;
import org.sparta.foodordermanagementservice.repository.CategoryRepository;
import org.sparta.foodordermanagementservice.repository.StoreRepository;
import org.sparta.foodordermanagementservice.service.StoreService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Store getStoreById(UUID storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Store not found with id: " + storeId));
    }

    @Override
    public Page<Store> getSearchStoreList(String keyword, double latitude, double longitude, int pageSize, int pageNumber, String sortBy, boolean isAsc) {
        // 키워드 검증
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Keyword cannot be null or empty.");
        }

        // 정렬 설정
        Sort sort = Sort.by(isAsc ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        // 키워드로 가게 이름이나 카테고리를 검색하여 삭제된 가게는 제외
        List<Store> searchResults = storeRepository.searchStores(keyword, pageRequest).getContent();

        // 거리 필터링을 적용하여 10km 이내의 가게만 포함
        List<Store> filteredStores = searchResults.stream()
                .filter(store -> calculateDistance(latitude, longitude, store.getLatitude(), store.getLongitude()) <= 10)
                .collect(Collectors.toList());

        // 페이지 객체로 변환하여 반환
        return new PageImpl<>(filteredStores, pageRequest, filteredStores.size());
    }

    // 거리 계산 로직
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS_KM = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }

    @Override
    @Transactional
    public Store registerStore(StoreRegistrationRequestDTO storeRegistrationRequestDTO, User user) {
        // 가게 이름 중복 체크
        checkStoreNameDuplication(storeRegistrationRequestDTO.getName());

        // 삭제되지 않은 카테고리 ID 목록으로 Category 엔티티 조회
        Set<Category> categories = getCategoriesByIds(storeRegistrationRequestDTO.getCategory());

        Store store = Store.builder()
                .user(user)
                .region(storeRegistrationRequestDTO.getRegion())
                .latitude(storeRegistrationRequestDTO.getLatitude())
                .longitude(storeRegistrationRequestDTO.getLongitude())
                .name(storeRegistrationRequestDTO.getName())
                .categories(categories)
                .createdAt(LocalDateTime.now())
                .createdBy(user.getUsername())
                .updatedAt(LocalDateTime.now())
                .updatedBy(user.getUsername())
                .build();

        return storeRepository.save(store);
    }

    @Override
    @Transactional
    public StoreUpdateResponseDTO updateStore(UUID storeId, StoreUpdateRequestDTO storeUpdateRequestDTO, User user) {
        // 가게 조회
        Store store = getStoreById(storeId);

        // 가게 이름 중복 체크
        checkStoreNameDuplication(storeUpdateRequestDTO.getName());

        // 카테고리 ID 목록으로 Category 엔티티 조회
        Set<Category> categories = getCategoriesByIds(storeUpdateRequestDTO.getCategory());

        // 가게 정보 수정
        store.setRegion(storeUpdateRequestDTO.getRegion());
        store.setLatitude(storeUpdateRequestDTO.getLatitude());
        store.setLongitude(storeUpdateRequestDTO.getLongitude());
        store.setName(storeUpdateRequestDTO.getName());
        store.setCategories(categories);
        store.setUpdatedAt(LocalDateTime.now());
        store.setUpdatedBy(user.getUsername());

        return toStoreUpdateResponseDTO(storeRepository.save(store));
    }

    @Override
    public Store deleteStore(UUID storeId, User user) {
        // 가게 존재 여부 확인
        checkStoreExists(storeId);

        Store store = getStoreById(storeId);

        store.setDeletedAt(LocalDateTime.now());
        store.setDeletedBy(user.getUsername());

        // 가게 삭제
        return storeRepository.save(store);
    }

    @Override
    public void checkStoreExists(UUID storeId) {
        if (!storeRepository.existsById(storeId)) {
            throw new IllegalArgumentException("Store not found with id: " + storeId);
        }
    }

    @Override
    public void checkStoreNameDuplication(String storeName) {
        boolean exists = storeRepository.existsByName(storeName);
        if (exists) {
            throw new IllegalArgumentException("Store with the same name already exists: " + storeName);
        }
    }

    @Override
    public Set<Category> getCategoriesByIds(List<UUID> categoryIds) {
        return categoryIds.stream()
                .map(categoryId -> {
                    Category category = categoryRepository.findById(categoryId)
                            .orElseThrow(() -> new IllegalArgumentException("Category not found: " + categoryId));

                    // 삭제된 카테고리 여부 확인
                    if (category.getDeletedAt() != null || category.getDeletedBy() != null) {
                        throw new IllegalArgumentException("Category is deleted and cannot be used: " + categoryId);
                    }

                    return category;
                })
                .collect(Collectors.toSet());
    }

    @Override
    public StoreUpdateResponseDTO toStoreUpdateResponseDTO(Store store) {
        Set<String> categoryNames = store.getCategories().stream()
                .map(Category::getName)
                .collect(Collectors.toSet());

        return StoreUpdateResponseDTO.builder()
                .id(store.getId())
                .name(store.getName())
                .region(store.getRegion())
                .latitude(store.getLatitude())
                .longitude(store.getLongitude())
                .categories(categoryNames)
                .updatedAt(store.getUpdatedAt())
                .updatedBy(store.getUpdatedBy())
                .build();
    }

    @Override
    public Store findByStoreId(UUID storeId) {
        return storeRepository.findById(storeId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_RESOURCE));
    }
}
