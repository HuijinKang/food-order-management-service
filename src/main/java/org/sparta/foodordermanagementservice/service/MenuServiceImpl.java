package org.sparta.foodordermanagementservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.dto.request.MenuRequestDto;
import org.sparta.foodordermanagementservice.dto.request.UpdateMenuRequestDto;
import org.sparta.foodordermanagementservice.dto.response.MenuResponseDto;
import org.sparta.foodordermanagementservice.entity.Menu;
import org.sparta.foodordermanagementservice.entity.MenuStatus;
import org.sparta.foodordermanagementservice.entity.Store;
import org.sparta.foodordermanagementservice.repository.MenuRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
//    private final StoreService storeService;

    // 메뉴 등록
    public void createMenu(UUID storeId, MenuRequestDto requestDto) {

//        Store store = storeService.findByStoreId(storeId);

        Menu menu = Menu.builder()
//                .store(store)
                .name(requestDto.getName())
                .price(requestDto.getPrice())
                .description(requestDto.getDescription())
                .status(MenuStatus.ACTIVE)
                .build();

        menuRepository.save(menu);
    }

    // 메뉴 수정
    @Transactional
    public void updateMenu(UUID menuId, UUID storeId, UpdateMenuRequestDto requestDto) {
        Menu existingMenu = menuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("해당하는 메뉴를 찾을 수 없습니다."));

//        Store store = storeService.findByStoreId(storeId);
//        if (!existingMenu.getStore().equals(store)) {
//            throw new RuntimeException("해당 가게에 등록된 메뉴가 아닙니다.");
//        }

        existingMenu.updateName(requestDto.getName());
        existingMenu.updatePrice(requestDto.getPrice());
        existingMenu.updateDescription(requestDto.getDescription());
        existingMenu.updateStatus(requestDto.getStatus());
    }

    // 메뉴 삭제
    @Transactional
    public void deleteMenu(UUID menuId, String username) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("해당하는 메뉴를 찾을 수 없습니다."));

        menu.updateStatus(MenuStatus.DISCONTINUED);
    }

    // 메뉴 단건 조회
    public MenuResponseDto getMenu(UUID menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("해당하는 메뉴를 찾을 수 없습니다."));

        return MenuResponseDto.builder()
                .storeId(menu.getStore().getId())
                .name(menu.getName())
                .price(menu.getPrice())
                .description(menu.getDescription())
                .build();
    }

    // 메뉴 목록 조회
    public List<MenuResponseDto> getMenus(UUID storeId) {
        List<Menu> menuList = menuRepository.findByStoreId(storeId);
        return menuList.stream()
                .map(menu -> MenuResponseDto.builder()
                        .storeId(menu.getStore().getId())
                        .name(menu.getName())
                        .price(menu.getPrice())
                        .description(menu.getDescription())
                        .build())
                .collect(Collectors.toList());
    }

}
