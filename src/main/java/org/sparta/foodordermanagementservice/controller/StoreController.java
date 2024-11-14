package org.sparta.foodordermanagementservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.common.ApiResponse;
import org.sparta.foodordermanagementservice.dto.request.StoreRegistrationRequestDTO;
import org.sparta.foodordermanagementservice.dto.request.StoreUpdateRequestDTO;
import org.sparta.foodordermanagementservice.dto.response.StoreUpdateResponseDTO;
import org.sparta.foodordermanagementservice.entity.Store;
import org.sparta.foodordermanagementservice.entity.UserRole;
import org.sparta.foodordermanagementservice.security.UserDetailsImpl;
import org.sparta.foodordermanagementservice.service.StoreService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreController {
    private final StoreService storeService;

    // 가게 단건 조회
    @GetMapping("/{storeId}")
    public ApiResponse<Store> getStoreById(@PathVariable UUID storeId) {
        return ApiResponse.ofSuccess(storeService.getStoreById(storeId));
    }

    // 10km 이내 가게명 및 카테고리명으로 검색
    @GetMapping("/search")
    public ApiResponse<Page<Store>> searchStore(@RequestParam String keyword,
                                                @RequestParam double latitude,
                                                @RequestParam double longitude,
                                                @RequestParam(defaultValue = "10") int pageSize,
                                                @RequestParam(defaultValue = "0") int pageNumber,
                                                @RequestParam(defaultValue = "createdAt") String sortedBy,
                                                @RequestParam(defaultValue = "true") boolean isAsc) {
        return ApiResponse.ofSuccess(storeService.getSearchStoreList(keyword, latitude, longitude, pageSize, pageNumber, sortedBy, isAsc));
    }

    // 가게 등록
    @PostMapping
    @Secured({UserRole.Authority.MASTER})
    public ApiResponse<Store> registerStore(@Valid @RequestBody StoreRegistrationRequestDTO storeRegistrationRequestDTO,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ApiResponse.ofSuccess(storeService.registerStore(storeRegistrationRequestDTO, userDetails.getUser()));
    }

    // 가게 정보 수정
    @PatchMapping("/{storeId}")
    @Secured({UserRole.Authority.OWNER, UserRole.Authority.MANAGER, UserRole.Authority.MASTER})
    public ApiResponse<StoreUpdateResponseDTO> updateStore(@PathVariable UUID storeId,
                                                           @Valid @RequestBody StoreUpdateRequestDTO storeUpdateRequestDTO,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ApiResponse.ofSuccess(storeService.updateStore(storeId, storeUpdateRequestDTO, userDetails.getUser()));
    }

    // 가게 삭제
    @DeleteMapping("/{storeId}")
    @Secured({UserRole.Authority.MASTER})
    public ApiResponse<Void> deleteStore(@PathVariable UUID storeId) {
        storeService.deleteStore(storeId);

        return ApiResponse.ofSuccess(null);
    }
}
