package org.sparta.foodordermanagementservice.service;

import org.sparta.foodordermanagementservice.dto.request.MenuRequestDto;
import org.sparta.foodordermanagementservice.dto.request.UpdateMenuRequestDto;
import org.sparta.foodordermanagementservice.dto.response.MenuResponseDto;

import java.util.List;
import java.util.UUID;

public interface MenuService {

    void createMenu(UUID storeId, MenuRequestDto requestDto);

    void updateMenu(UUID menuId, UUID storeId, UpdateMenuRequestDto requestDto);

    void deleteMenu(UUID menuId, String username);

    MenuResponseDto getMenu(UUID menuId);

    List<MenuResponseDto> getMenus(UUID storeId);
}
