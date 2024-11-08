package org.sparta.foodordermanagementservice.controller;

import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.common.ApiResponse;
import org.sparta.foodordermanagementservice.entity.Store;
import org.sparta.foodordermanagementservice.service.StoreService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;

    // 주문 가능한 가게 조회
    @GetMapping("/nearby")
    public ApiResponse<List<Store>> getNearbyStores(@RequestParam double latitude, @RequestParam double longitude) {
        return ApiResponse.ofSuccess(storeService.getStoresWithinRadius(latitude, longitude));
    }

    // 가게 단건 조회
    @GetMapping("/{storeId}")
    public ApiResponse<Store> getStoreById(@PathVariable UUID storeId) {
        return ApiResponse.ofSuccess(storeService.getStoreById(storeId));
    }
}
