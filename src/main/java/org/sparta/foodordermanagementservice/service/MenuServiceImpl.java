package org.sparta.foodordermanagementservice.service;

import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.dto.request.MenuRequestDto;
import org.sparta.foodordermanagementservice.entity.Menu;
import org.sparta.foodordermanagementservice.entity.MenuStatus;
import org.sparta.foodordermanagementservice.entity.Store;
import org.sparta.foodordermanagementservice.repository.MenuRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
//    private final StoreService storeService;

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


}
