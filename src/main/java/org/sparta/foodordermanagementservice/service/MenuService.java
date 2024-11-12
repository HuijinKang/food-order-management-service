package org.sparta.foodordermanagementservice.service;

import org.sparta.foodordermanagementservice.dto.request.MenuRequestDto;
import org.sparta.foodordermanagementservice.dto.request.UpdateMenuRequestDto;

import java.util.UUID;

public interface MenuService {

    void createMenu(UUID storeId, MenuRequestDto requestDto);

    void updateMenu(UUID menuId, UUID storeId, UpdateMenuRequestDto requestDto);
}
