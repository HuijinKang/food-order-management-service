package org.sparta.foodordermanagementservice.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.foodordermanagementservice.common.exeption.CustomException;
import org.sparta.foodordermanagementservice.common.exeption.ErrorCode;
import org.sparta.foodordermanagementservice.dto.*;
import org.sparta.foodordermanagementservice.dto.response.ResOrderedMenu;
import org.sparta.foodordermanagementservice.dto.response.ResPagedOrderObj;
import org.sparta.foodordermanagementservice.dto.response.ResReadOrderDetail;
import org.sparta.foodordermanagementservice.entity.Menu;
import org.sparta.foodordermanagementservice.entity.MenuStatus;
import org.sparta.foodordermanagementservice.entity.enumerate.PaymentStatus;
import org.sparta.foodordermanagementservice.repository.MenuRepository;
import org.sparta.foodordermanagementservice.repository.OrderRepository;
import org.sparta.foodordermanagementservice.repository.OrderedMenuRepository;
import org.sparta.foodordermanagementservice.repository.PaymentRepository;
import org.sparta.foodordermanagementservice.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.sparta.foodordermanagementservice.common.utils.RoleUtils.*;

@Slf4j


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepo;
    private final OrderedMenuRepository orderedMenuRepo;
    private final PaymentRepository paymentRepo;
    private final MenuRepository menuRepo;


    @Override
    public Page<ResPagedOrderObj> paginateOrders(PaginateOrdersDTO dto,
                                                 UserDetails userDetails) {

        log.info("dto at service" + dto.toString());

        List<ResPagedOrderObj> pageContent
                = orderRepo.readCurrentPageOrders(dto)
                .stream()
                .map(orderDTO -> {

                    eraseNotAllowedInfo(orderDTO, userDetails);
                    ResPagedOrderObj res = ResPagedOrderObj.from(orderDTO);
                    ;
                    return res;
                })
                .toList();

        long totalOrders
                = orderRepo.countTotalOrders(dto.getStoreId(), dto.getUsername());

        Pageable pageable
                = PageRequest.of(dto.getPageNumber(), dto.getPageSize());

        return new PageImpl<>(pageContent, pageable, totalOrders);
    }

    @Override
    public void deleteOrder(UUID orderId, UserDetails userDetails) {

        String deleterName
                = userDetails.getUsername();

        orderRepo.deleteOrder(orderId, deleterName);
    }


    @Override
    @Transactional
    public UUID createOrder(CreateOrderDto dto) {

        // 주문 동안 메뉴의 가격/상태/삭제 여부가 변경되었는지 확인
        Map<UUID, Integer> currentMenuPriceMap;
        List<Menu> menuList
                = menuRepo.findAllById
                (
                        dto.getOrderedMenuInfos()
                                .stream()
                                .map(OrderedMenuInfo::getMenuId)
                                .toList()
                );
        log.info("at service menuentities" + menuList.get(0).toString() + " " + dto.getOrderedMenuInfos().get(0).toString());
        currentMenuPriceMap = new HashMap<>();
////                = menuList.stream()
//                .filter(menu ->
//                        menu.getStatus() != MenuStatus.ACTIVE)
//                .collect(Collectors.toMap(Menu::getId, Menu::getPrice));
        menuList.forEach(menu -> {
            if (menu.getStatus() != MenuStatus.ACTIVE) {
                throw new CustomException(ErrorCode.MENU_CHANGED);
            }
//            if (menu.getDeletedAt() != null) {/*todo 강현님 삭제정보 추가시 주석 해제, filter가 아니라 exception처리*/ //menu.getDeletedAt() == null ||
//                throw new CustomException(ErrorCode.MENU_DELETED);
//            }
            currentMenuPriceMap.put(menu.getId(), menu.getPrice());
        });

        log.info("currentMenuPriceMap : " + currentMenuPriceMap.toString());

        dto.getOrderedMenuInfos().forEach(menuInfo ->
        {
            if (!currentMenuPriceMap.containsKey(menuInfo.getMenuId())) {
                throw new CustomException(ErrorCode.MENU_DELETED);

            } else if (menuInfo.getMenuPrice()
                    != currentMenuPriceMap.get(menuInfo.getMenuId())) {
                throw new CustomException(ErrorCode.MENU_PRICE_CHANGED);

            }
        });

        PaymentDTO orderPayment;
        int totalPrice
                = dto.getOrderedMenuInfos()
                .stream()
                .mapToInt(menuInfo ->
                        menuInfo.getMenuPrice() * menuInfo.getAmount())
                .sum();
        orderPayment
                = PaymentDTO.builder()
                .payedPrice(totalPrice)
                .status(PaymentStatus.PAY_WAIT)
                .build();
        paymentRepo.createPayment(orderPayment);

        UUID createdOrderId
                = orderRepo.createOrder(dto);

        return createdOrderId;
    }

    @Override
    public ResReadOrderDetail readOrderDetail(UUID orderId, UserDetails userDetails) {

        OrderDTO order = orderRepo.readOrder(orderId);

        List<ResOrderedMenu> menuList
                = orderedMenuRepo.readOrderedMenuList(orderId)
                .stream()
                .map(ResOrderedMenu::from)
                .toList();

        PaymentDTO payment
                = hasManagerRole(userDetails) || hasCustomerRole(userDetails)
                ? PaymentDTO.from(paymentRepo.readPayment())
                : null;

        return ResReadOrderDetail.from(order, menuList, payment);
    }

    @Override
    public void updateOrder(UUID orderId, UpdateOrderStatusDto dto) {

        orderRepo.updateOrderStatus(orderId, dto);
    }

    protected void eraseNotAllowedInfo(OrderDTO target,
                                       UserDetails userDetails) {

        if (hasOwnerRole(userDetails) || hasCustomerRole(userDetails)) {
            target.setDeletedAt(null);
            target.setDeletedBy(null);
        }
    }


}
