package org.sparta.foodordermanagementservice.controller;

import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.common.ApiResponse;
import org.sparta.foodordermanagementservice.dto.request.MenuRequestDto;
import org.sparta.foodordermanagementservice.dto.request.UpdateMenuRequestDto;
import org.sparta.foodordermanagementservice.dto.response.MenuResponseDto;
import org.sparta.foodordermanagementservice.service.MenuService;
import org.springframework.data.domain.Page;
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
//    @PreAuthorize("hasAnyRole('OWNER', 'MASTER')")
    @PostMapping
    public ApiResponse<?> createMenu(@RequestParam(required = false) UUID storeId,
                                     @RequestBody MenuRequestDto requestDto) {
        menuService.createMenu(storeId, requestDto);
        return ApiResponse.ofSuccess(null);
    }

    // 메뉴 수정
//    @PreAuthorize("hasAnyRole('OWNER', 'MASTER')")
    @PatchMapping("/{menuId}")
    public ApiResponse<?> updateMenu(@PathVariable UUID menuId,
                                     @RequestParam UUID storeId,
                                     @RequestBody UpdateMenuRequestDto requestDto) {
        menuService.updateMenu(menuId, storeId, requestDto);
        return ApiResponse.ofSuccess(null);
    }

    // 메뉴 삭제
//    @PreAuthorize("hasAnyRole('OWNER', 'MASTER')")
    @PatchMapping("/{menuId}")
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
    @GetMapping("/api/menus")
    public ApiResponse<List<MenuResponseDto>> getMenus(@RequestParam UUID storeId) {
        List<MenuResponseDto> menuList = menuService.getMenus(storeId);
        return ApiResponse.ofSuccess(menuList);
    }

    // 메뉴 검색
    @GetMapping("/search")
    public ApiResponse<Page<MenuResponseDto>> searchMenus(
            @RequestParam String condition,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "name") String sortedBy,
            @RequestParam(defaultValue = "true") boolean isAsc) {

        Page<MenuResponseDto> menuPage = menuService.searchMenus(condition, keyword, pageSize, pageNumber, sortedBy, isAsc);
        return ApiResponse.ofSuccess(menuPage);
    }

}
