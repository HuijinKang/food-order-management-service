package org.sparta.foodordermanagementservice.service;

import org.sparta.foodordermanagementservice.dto.request.MenuRequestDto;

import java.util.UUID;

public interface MenuService {

    void createMenu(UUID storeId, MenuRequestDto requestDto);
}
