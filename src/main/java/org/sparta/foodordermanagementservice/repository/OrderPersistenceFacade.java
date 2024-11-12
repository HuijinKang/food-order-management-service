package org.sparta.foodordermanagementservice.repository;


import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.dto.OrderDTO;
import org.sparta.foodordermanagementservice.dto.SelectOrderListDTO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("UnnecessaryLocalVariable")


@RequiredArgsConstructor
@Repository
public class OrderPersistenceFacade {

    private final OrderRepository orderRepo;
    //    private final PaymentRepository paymentRepo;


    @Transactional(readOnly = true)
    public List<OrderDTO> readOrderList(SelectOrderListDTO dto) {

        List<OrderDTO> readOrderDtoList
                = orderRepo.readOrderList(dto)
                .stream()
                .map(OrderDTO::from)
                .toList();

        return readOrderDtoList;
    }

    @Transactional
    public void deleteOrder(UUID orderId, String deleterName) {
        orderRepo.softDeleteOrder(orderId, deleterName);
    }
}
