package org.sparta.foodordermanagementservice.repository;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.foodordermanagementservice.common.exeption.CustomException;
import org.sparta.foodordermanagementservice.common.exeption.ErrorCode;
import org.sparta.foodordermanagementservice.dto.*;
import org.sparta.foodordermanagementservice.entity.Order;
import org.sparta.foodordermanagementservice.entity.OrderedMenu;
import org.sparta.foodordermanagementservice.entity.enumerate.OrderStatus;
import org.sparta.foodordermanagementservice.entity.enumerate.OrderType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@SuppressWarnings("UnnecessaryLocalVariable")
@Slf4j

@RequiredArgsConstructor
@Repository
public class OrderRepository {
    private final UserRepository userRepository;

    private final OrderDAO orderDao;
    private final OrderedMenuDAO orderedMenuDao;
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;


    @Transactional(readOnly = true)
    public List<OrderDTO> readCurrentPageOrders(PaginateOrdersDTO dto) {

        log.info("my repository " + dto.toString());
        List<OrderDTO> currentPageOrderDTOs
                = orderDao.readCurrentPage(dto)
                .stream()
                .map(OrderDTO::from)
                .toList();


        return currentPageOrderDTOs;
    }

    @Transactional
    public void deleteOrder(UUID orderId, String deleterName) {
        orderDao.softDeleteOrder(orderId, deleterName);
    }

    public long countTotalOrders(UUID storeId, String username) {

        return orderDao.countTotal(storeId, username);
    }

    public OrderDTO readOrder(UUID orderId) {

        return OrderDTO.from(orderDao.readOrder(orderId));
    }

    @Transactional
    public UUID createOrder(CreateOrderDto dto) {

        Order createdOrder;
        Order orderToCreate
                = Order.builder()
                .user(userRepository.findByUsername(dto.getUsername())
                        .orElseThrow(() -> new IllegalArgumentException("User not found"))
                )
                .store(storeRepository.findById(dto.getStoreId())
                        .orElseThrow(() -> new IllegalArgumentException("Store not found"))
                )
                .status(OrderStatus.WAIT)
                .type(OrderType.DELIVERY)
                .address(dto.getAddress())
                .comment(dto.getComment())
                .totalPrice(dto.getTotalPrice())
                .build();
        createdOrder
                = orderDao.createOrder(orderToCreate);


        Consumer<OrderedMenuInfo> createOrderedMenu
                = menuInfo ->
        {
            OrderedMenu orderedMenu
                    = OrderedMenu.builder()
                    .order(createdOrder)
                    .menu(menuRepository.findById(menuInfo.getMenuId())
                            .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND))
                    )
                    .amount(menuInfo.getAmount())
                    .build();

            orderedMenuDao.create(orderedMenu);
        };

        dto.getOrderedMenuInfos().forEach(createOrderedMenu);


        return createdOrder.getId();
    }

    public void updateOrderStatus(UUID orderId, UpdateOrderStatusDto dto) {

        orderDao.updateStatus(orderId, dto.getOrderStatus());
    }

}
