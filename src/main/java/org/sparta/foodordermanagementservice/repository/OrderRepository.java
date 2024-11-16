package org.sparta.foodordermanagementservice.repository;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.foodordermanagementservice.dto.OrderDTO;
import org.sparta.foodordermanagementservice.dto.PaginateOrdersDTO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("UnnecessaryLocalVariable")
@Slf4j

@RequiredArgsConstructor
@Repository
public class OrderRepository {

    private final OrderDAO orderDao;
    private final OrderedMenuDAO orderedMenuDao;


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

//    public UUID createOrder(CreateOrderDto dto) {
//
//            UUID createdId = orderedMenuDao.createOrder(dto);
//
//            return createdId;
//    }
}
