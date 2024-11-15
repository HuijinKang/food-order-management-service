package org.sparta.foodordermanagementservice.controller;

import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.common.ApiResponse;
import org.sparta.foodordermanagementservice.dto.request.MenuRequestDto;
import org.sparta.foodordermanagementservice.dto.request.SearchRequestDto;
import org.sparta.foodordermanagementservice.dto.request.UpdateMenuRequestDto;
import org.sparta.foodordermanagementservice.dto.response.MenuResponseDto;
import org.sparta.foodordermanagementservice.entity.UserRole;
import org.sparta.foodordermanagementservice.service.MenuService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    // 메뉴 등록
    @Secured({UserRole.Authority.OWNER, UserRole.Authority.MASTER})
    @PostMapping
    public ApiResponse<?> createMenu(@RequestParam UUID storeId,
                                     @ModelAttribute MenuRequestDto requestDto) {
        menuService.createMenu(storeId, requestDto);
        return ApiResponse.ofSuccess(null);
    }

    // 메뉴 수정
    @Secured({UserRole.Authority.OWNER, UserRole.Authority.MASTER})
    @PatchMapping("/{menuId}")
    public ApiResponse<?> updateMenu(@PathVariable UUID menuId,
                                     @RequestParam UUID storeId,
                                     @ModelAttribute UpdateMenuRequestDto requestDto) {
        menuService.updateMenu(menuId, storeId, requestDto);
        return ApiResponse.ofSuccess(null);
    }

    // 메뉴 삭제
    @Secured({UserRole.Authority.OWNER, UserRole.Authority.MASTER})
    @DeleteMapping("/{menuId}")
    public ApiResponse<?> deleteMenu(@PathVariable UUID menuId,
                                     @AuthenticationPrincipal UserDetails userDetails) {
        menuService.deleteMenu(menuId, userDetails.getUsername());
        return ApiResponse.ofSuccess(null);
    }

    // 메뉴 단건 조회
    @GetMapping("/{menuId}")
    public ApiResponse<MenuResponseDto> getMenu(@PathVariable UUID menuId) {
        return ApiResponse.ofSuccess(menuService.getMenu(menuId));
    }

    // 메뉴 목록 조회
    @GetMapping
    public ApiResponse<List<MenuResponseDto>> getMenus(@RequestParam UUID storeId) {
        List<MenuResponseDto> menuList = menuService.getMenus(storeId);
        return ApiResponse.ofSuccess(menuList);
    }

    // 메뉴 검색
    @GetMapping("/search")
    public ApiResponse<Page<MenuResponseDto>> searchMenus(
            @ModelAttribute SearchRequestDto searchRequestDto) {
        Page<MenuResponseDto> menuPage = menuService.searchMenus(
                searchRequestDto.getCondition(),
                searchRequestDto.getKeyword(),
                searchRequestDto.getPageSize(),
                searchRequestDto.getPageNumber(),
                searchRequestDto.getSortedBy(),
                searchRequestDto.isAsc()
        );
        return ApiResponse.ofSuccess(menuPage);
    }

}
