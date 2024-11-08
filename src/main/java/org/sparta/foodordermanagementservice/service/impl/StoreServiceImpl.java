package org.sparta.foodordermanagementservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.entity.Store;
import org.sparta.foodordermanagementservice.repository.StoreRepository;
import org.sparta.foodordermanagementservice.service.StoreService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;

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
}
