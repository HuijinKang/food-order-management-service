package org.sparta.foodordermanagementservice.repository;


import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.dto.OrderDTO;
import org.sparta.foodordermanagementservice.dto.SelectOrderListDTO;
import org.sparta.foodordermanagementservice.entity.Order;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
@Transactional
public class OrderTransactionalFacade {

    private final OrderRepository orderRepo;

    //    private final PaymentRepository paymentRepo;


    public List<OrderDTO> readOrderList(SelectOrderListDTO dto) {

        List<Order> readOrderList
                = orderRepo.readOrderList(dto);

        List<OrderDTO> readOrderDtoList
                = readOrderList.stream()
                .map(OrderDTO::from)
                .toList();

        return readOrderDtoList;
    }

    public void deleteOrder(UUID orderId) {
        orderRepo.softDeleteOrder(orderId);
    }
}
