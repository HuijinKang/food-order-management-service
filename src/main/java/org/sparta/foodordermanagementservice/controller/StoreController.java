package org.sparta.foodordermanagementservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.common.ApiResponse;
import org.sparta.foodordermanagementservice.dto.request.SearchRequestDto;
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
    public ApiResponse<Page<Store>> searchStore(@RequestParam double latitude,
                                                @RequestParam double longitude,
                                                @RequestBody SearchRequestDto searchRequestDto) {
        return ApiResponse.ofSuccess(storeService.getSearchStoreList(latitude, longitude, searchRequestDto));
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
    public ApiResponse<Store> deleteStore(@PathVariable UUID storeId,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ApiResponse.ofSuccess(storeService.deleteStore(storeId, userDetails.getUser()));
    }
}
