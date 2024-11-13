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
    //    private final PaymentDAO paymentDao;


    @Transactional(readOnly = true)
    public List<OrderDTO> readCurrentPageOrders(PaginateOrdersDTO dto) {

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

    public long countTotalPages(PaginateOrdersDTO dto) {

        return orderDao.countTotalPages(dto);
    }
}
