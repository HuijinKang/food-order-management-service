package org.sparta.foodordermanagementservice.service.Impl;

import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.dto.request.StoreRegistrationRequestDTO;
import org.sparta.foodordermanagementservice.dto.request.StoreUpdateRequestDTO;
import org.sparta.foodordermanagementservice.dto.response.StoreSearchResponseDTO;
import org.sparta.foodordermanagementservice.dto.response.StoreUpdateResponseDTO;
import org.sparta.foodordermanagementservice.entity.Category;
import org.sparta.foodordermanagementservice.entity.Store;
import org.sparta.foodordermanagementservice.entity.User;
import org.sparta.foodordermanagementservice.repository.CategoryRepository;
import org.sparta.foodordermanagementservice.repository.StoreRepository;
import org.sparta.foodordermanagementservice.service.StoreService;
import org.springframework.data.domain.Page;
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
    public List<StoreSearchResponseDTO> getStoresWithinRadius(double latitude, double longitude) {
        List<Store> allStores = storeRepository.findAll();

        return allStores.stream()
                .filter(store -> calculateDistance(latitude, longitude, store.getLatitude(), store.getLongitude()) <= 10)   // 10km
                .map(this::toStoreSearchResponseDTO) // Store 엔티티를 StoreDTO로 변환
                .collect(Collectors.toList());
    }

    @Override
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS = 6371; // 지구 반경 (km)

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    @Override
    public Store getStoreById(UUID storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Store not found with id: " + storeId));
    }

    @Override
    public Page<Store> getSearchStoreList(String keyword, int pageSize, int pageNumber, String sortBy, boolean isAsc) {

        // 정렬 설정
        Sort sort = Sort.by(isAsc ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        // 가게 이름이나 카테고리를 키워드로 검색
        return storeRepository.searchStores(keyword, keyword, pageRequest);
    }

    @Override
    @Transactional
    public Store registerStore(StoreRegistrationRequestDTO storeRegistrationRequestDTO, User user) {
        // 가게 이름 중복 체크
        checkStoreNameDuplication(storeRegistrationRequestDTO.getName());

        // 카테고리 ID 목록으로 Category 엔티티 조회
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
    public void deleteStore(UUID storeId) {
        // 가게 존재 여부 확인
        checkStoreExists(storeId);

        // 가게 삭제
        storeRepository.deleteById(storeId);
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
                .map(categoryId -> categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new IllegalArgumentException("Category not found: " + categoryId)))
                .collect(Collectors.toSet());
    }

    @Override
    public StoreSearchResponseDTO toStoreSearchResponseDTO(Store store) {
        Set<String> categoryNames = store.getCategories().stream()
                .map(Category::getName)
                .collect(Collectors.toSet());

        return new StoreSearchResponseDTO(
                store.getId(),
                store.getName(),
                store.getRegion(),
                store.getLatitude(),
                store.getLongitude(),
                categoryNames,
                store.getTotalRating(),
                store.getReviewCount()
        );
    }

    @Override
    public StoreUpdateResponseDTO toStoreUpdateResponseDTO(Store store) {
        Set<String> categoryNames = store.getCategories().stream()
                .map(Category::getName)
                .collect(Collectors.toSet());

        return new StoreUpdateResponseDTO(
                store.getId(),
                store.getName(),
                store.getRegion(),
                store.getLatitude(),
                store.getLongitude(),
                categoryNames,
                store.getUpdatedAt(),
                store.getUpdatedBy()
        );
    }
}
