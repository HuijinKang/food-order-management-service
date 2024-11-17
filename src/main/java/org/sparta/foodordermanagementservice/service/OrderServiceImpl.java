package org.sparta.foodordermanagementservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.foodordermanagementservice.common.exeption.CustomException;
import org.sparta.foodordermanagementservice.common.exeption.ErrorCode;
import org.sparta.foodordermanagementservice.dto.*;
import org.sparta.foodordermanagementservice.dto.response.ResOrderedMenu;
import org.sparta.foodordermanagementservice.dto.response.ResPagedOrderObj;
import org.sparta.foodordermanagementservice.dto.response.ResReadOrderDetail;
import org.sparta.foodordermanagementservice.entity.Menu;
import org.sparta.foodordermanagementservice.entity.UserRole;
import org.sparta.foodordermanagementservice.entity.enumerate.PaymentStatus;
import org.sparta.foodordermanagementservice.repository.MenuRepository;
import org.sparta.foodordermanagementservice.repository.OrderRepository;
import org.sparta.foodordermanagementservice.repository.OrderedMenuRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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

        List<ResPagedOrderObj> pageContent
                = orderRepo.readCurrentPageOrders(dto)
                .stream()
                .map(orderDTO -> {
                    log.info("service " + orderDTO.toString());
                    eraseNotAllowedInfo(orderDTO, userDetails.getAuthorities());
                    ResPagedOrderObj res = ResPagedOrderObj.from(orderDTO);
                    log.info("res herer" + res.toString());
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

        Map<UUID, Integer> currentMenuPriceMap;
        List<Menu> orderedMenuList
                = menuRepo.findAllById(
                dto.getOrderedMenuInfos()
                        .stream()
                        .map(OrderedMenuInfo::getMenuId)
                        .toList());
        currentMenuPriceMap
                = orderedMenuList.stream()
                .filter(menu -> menu.getDeletedAt() == null)
                .collect(Collectors.toMap(Menu::getId, Menu::getPrice));

        dto.getOrderedMenuInfos().forEach(menuInfo ->
        {
            if (!currentMenuPriceMap.containsKey(menuInfo.getMenuId())) {
                throw new CustomException(ErrorCode.MENU_DELETED);

            } else if (menuInfo.getMenuPrice() != currentMenuPriceMap.get(menuInfo.getMenuId())) {

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

        boolean paymentInfoAccessible
                = userDetails.getAuthorities().contains(UserRole.Authority.MANAGER)
                || userDetails.getAuthorities().contains(UserRole.Authority.CUSTOMER);

        PaymentDTO payment
                = paymentInfoAccessible
                ? PaymentDTO.from(paymentRepo.readPayment())
                : null;

        return ResReadOrderDetail.from(order, menuList, payment);
    }

    protected void eraseNotAllowedInfo(OrderDTO target,
                                       Collection<? extends GrantedAuthority> authorities) {

        if (authorities.contains(UserRole.Authority.OWNER)
                || authorities.contains(UserRole.Authority.CUSTOMER)) {

            target.setDeletedAt(null);
            target.setDeletedBy(null);
        }

    }


}
