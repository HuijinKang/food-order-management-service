package org.sparta.foodordermanagementservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.common.ApiResponse;
import org.sparta.foodordermanagementservice.common.exeption.ErrorCode;
import org.sparta.foodordermanagementservice.dto.request.StoreRequest;
import org.sparta.foodordermanagementservice.entity.Store;
import org.sparta.foodordermanagementservice.security.UserDetailsImpl;
import org.sparta.foodordermanagementservice.service.StoreService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    // 가게 검색
    @GetMapping("/search")
    public ApiResponse<List<Store>> searchStore(@RequestParam String condition,
                                                @RequestParam String keyword,
                                                @RequestParam int pageSize,
                                                @RequestParam int pageNumber,
                                                @RequestParam String sortedBy,
                                                @RequestParam boolean isAsc) {
        return ApiResponse.ofSuccess(storeService.getSearchStoreList(condition, keyword, pageSize, pageNumber, sortedBy, isAsc));
    }

    // 가게 등록
//    @PreAuthorize("hasAuthority('ROLE_MASTER')")
    @PostMapping
    public ApiResponse<Store> registerStore(@RequestBody StoreRequest storeRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String userRole = userDetails.getAuthorities().toString();
        if (!userRole.equals("ROLE_MASTER")) ApiResponse.ofError(ErrorCode.BAD_REQUEST);

        return ApiResponse.ofSuccess(storeService.registerStore(storeRequest, userDetails.getUser()));
    }

    // 가게 정보 수정
//    @PreAuthorize("hasAuthority('ROLE_MASTER') or hasAuthority('ROLE_OWNER')")
    @PatchMapping("/{storeId}")
    public ApiResponse<Store> updateStore(@PathVariable UUID storeId, @Valid @RequestBody StoreRequest storeRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String userRole = userDetails.getAuthorities().toString();
        if (!userRole.equals("ROLE_MASTER") || userRole.equals("ROLE_OWNER")) ApiResponse.ofError(ErrorCode.BAD_REQUEST);

        return ApiResponse.ofSuccess(storeService.updateStore(storeId, storeRequest, userDetails.getUser()));
    }

    // 가게 삭제
//    @PreAuthorize("hasAuthority('ROLE_MASTER')")
    @DeleteMapping("/{storeId}")
    public ApiResponse<Void> deleteStore(@PathVariable UUID storeId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String userRole = userDetails.getAuthorities().toString();
        if (!userRole.equals("ROLE_MASTER")) ApiResponse.ofError(ErrorCode.BAD_REQUEST);

        storeService.deleteStore(storeId);
        return ApiResponse.ofSuccess(null);
    }
}
