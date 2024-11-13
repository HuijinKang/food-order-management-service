package org.sparta.foodordermanagementservice.service;

import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.dto.request.StoreRequest;
import org.sparta.foodordermanagementservice.entity.Category;
import org.sparta.foodordermanagementservice.entity.Store;
import org.sparta.foodordermanagementservice.entity.User;
import org.sparta.foodordermanagementservice.repository.CategoryRepository;
import org.sparta.foodordermanagementservice.repository.StoreRepository;
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
    public List<Store> getStoresWithinRadius(double latitude, double longitude) {
        List<Store> allStores = storeRepository.findAll();

        return allStores.stream()
                .filter(store -> calculateDistance(latitude, longitude, store.getLatitude(), store.getLongitude()) <= 10)
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
//        Sort sort = Sort.by(isAsc ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
//        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
//
//        Page<Store> resultPage;
//        if ("category".equalsIgnoreCase(condition)) {
//            resultPage = storeRepository.findByCategoriesNameContaining(keyword, pageRequest);
//        } else if ("name".equalsIgnoreCase(condition)) {
//            resultPage = storeRepository.findByNameContaining(keyword, pageRequest);
//        } else {
//            throw new IllegalArgumentException("지원하지 않는 검색 조건입니다.");
//        }
//
//        return resultPage.getContent();

        // 정렬 설정
        Sort sort = Sort.by(isAsc ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        // 가게 이름이나 카테고리를 키워드로 검색
        return storeRepository.searchStores(keyword, keyword, pageRequest);
    }

    @Override
    @Transactional
    public Store registerStore(StoreRequest storeRequest, User user) {
        // 카테고리 ID 목록으로 Category 엔티티 조회
        Set<Category> categories = storeRequest.getCategory().stream()
                .map(categoryId -> categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new IllegalArgumentException("Category not found: " + categoryId)))
                .collect(Collectors.toSet());

        Store store = Store.builder()
                .region(storeRequest.getRegion())
                .latitude(storeRequest.getLatitude())
                .longitude(storeRequest.getLongitude())
                .name(storeRequest.getName())
                .categories(categories)
                .createdAt(LocalDateTime.now())
                .createdBy(user.getUsername())
                .build();

        return storeRepository.save(store);
    }

    @Override
    @Transactional
    public Store updateStore(UUID storeId, StoreRequest storeRequest, User user) {
        // 가게 조회
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Store not found with id: " + storeId));

        // 카테고리 ID 목록으로 Category 엔티티 조회
        Set<Category> categories = storeRequest.getCategory().stream()
                .map(categoryId -> categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new IllegalArgumentException("Category not found: " + categoryId)))
                .collect(Collectors.toSet());

        // 가게 정보 수정
        store.setRegion(storeRequest.getRegion());
        store.setLatitude(storeRequest.getLatitude());
        store.setLongitude(storeRequest.getLongitude());
        store.setName(storeRequest.getName());
        store.setCategories(categories);
        store.setUpdatedAt(LocalDateTime.now());
        store.setUpdatedBy(user.getUsername());

        return storeRepository.save(store);
    }

    @Override
    public void deleteStore(UUID storeId) {
        // 가게가 존재하는지 확인
        if (!storeRepository.existsById(storeId)) {
            throw new IllegalArgumentException("Store not found with id: " + storeId);
        }
        // 가게 삭제
        storeRepository.deleteById(storeId);
    }
}
