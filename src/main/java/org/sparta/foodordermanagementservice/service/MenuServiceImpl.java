package org.sparta.foodordermanagementservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.common.exeption.CustomException;
import org.sparta.foodordermanagementservice.common.exeption.ErrorCode;
import org.sparta.foodordermanagementservice.dto.request.MenuRequestDto;
import org.sparta.foodordermanagementservice.dto.request.UpdateMenuRequestDto;
import org.sparta.foodordermanagementservice.dto.response.MenuResponseDto;
import org.sparta.foodordermanagementservice.entity.Menu;
import org.sparta.foodordermanagementservice.entity.MenuStatus;
import org.sparta.foodordermanagementservice.entity.Store;
import org.sparta.foodordermanagementservice.repository.MenuRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final StoreService storeService;
    private final ImageService imageService;

    // 메뉴 등록
    @Transactional
    @Override
    public void createMenu(UUID storeId, MenuRequestDto requestDto) {

        Store store = storeService.findByStoreId(storeId);

        String menuImageUrl = imageService.uploadFile(requestDto.getFile());

        Menu menu = Menu.builder()
                .store(store)
                .name(requestDto.getName())
                .price(requestDto.getPrice())
                .description(requestDto.getDescription())
                .status(MenuStatus.ACTIVE)
                .menuImageUrl(menuImageUrl)
                .build();

        menuRepository.save(menu);
    }

    // 메뉴 수정
    @Transactional
    @Override
    public void updateMenu(UUID menuId, UUID storeId, UpdateMenuRequestDto requestDto) {
        Menu existingMenu = menuRepository.findById(menuId)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));

        if (existingMenu.getStatus() == MenuStatus.DISCONTINUED) {
            throw new CustomException(ErrorCode.MENU_UPDATE_FAILED);
        }

        Store store = storeService.findByStoreId(storeId);
        if (!existingMenu.getStore().equals(store)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        existingMenu.updateName(requestDto.getName());
        existingMenu.updatePrice(requestDto.getPrice());
        existingMenu.updateDescription(requestDto.getDescription());
        existingMenu.updateStatus(requestDto.getStatus());
    }

    // 메뉴 삭제
    @Transactional
    @Override
    public void deleteMenu(UUID menuId, String username) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));

        if (menu.getStatus() == MenuStatus.DISCONTINUED) {
            throw new CustomException(ErrorCode.MENU_DELETE_FAILED);
        }

        menu.updateStatus(MenuStatus.DISCONTINUED);
    }

    // 메뉴 단건 조회
    @Override
    public MenuResponseDto getMenu(UUID menuId) {
        Menu menu = menuRepository.findByIdAndStatusNot(menuId, MenuStatus.DISCONTINUED)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));

        return MenuResponseDto.builder()
                .storeId(menu.getStore().getId())
                .name(menu.getName())
                .price(menu.getPrice())
                .description(menu.getDescription())
                .build();
    }

    // 메뉴 목록 조회
    @Override
    public List<MenuResponseDto> getMenus(UUID storeId) {
        List<Menu> menuList = menuRepository.findByStoreIdAndStatusNot(storeId, MenuStatus.DISCONTINUED);
        return menuList.stream()
                .map(menu -> MenuResponseDto.builder()
                        .storeId(menu.getStore().getId())
                        .name(menu.getName())
                        .price(menu.getPrice())
                        .description(menu.getDescription())
                        .build())
                .collect(Collectors.toList());
    }

    // 메뉴 검색
    @Override
    public Page<MenuResponseDto> searchMenus(String condition, String keyword, int pageSize,
                                             int pageNumber, String sortedBy, boolean isAsc) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize,
                isAsc ? Sort.by(sortedBy).ascending() : Sort.by(sortedBy).descending());

        Page<Menu> menuPage = menuRepository.searchMenus(condition, keyword, pageable);

        return menuPage.map(menu -> MenuResponseDto.builder()
                .storeId(menu.getStore().getId())
                .name(menu.getName())
                .price(menu.getPrice())
                .description(menu.getDescription())
                .build());
    }


}
