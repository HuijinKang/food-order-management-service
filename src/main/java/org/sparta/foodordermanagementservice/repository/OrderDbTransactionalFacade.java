package org.sparta.foodordermanagementservice.repository;


import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.dto.OrderDTO;
import org.sparta.foodordermanagementservice.dto.SelectOrderListDTO;
import org.sparta.foodordermanagementservice.entity.Order;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
public class OrderDbTransactionalFacade {

    private final OrderRepository orderRepo;

    //    private final PaymentRepository paymentRepo;


    public List<OrderDTO> selectOrderList(SelectOrderListDTO dto) {

        List<Order> selectedOrderList
                = orderRepo.selectOrderList(dto);

        return selectedOrderList.stream()
                .map(OrderDTO::from)
                .toList();
    }

}
