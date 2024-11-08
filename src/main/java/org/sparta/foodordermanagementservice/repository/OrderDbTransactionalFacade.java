package org.sparta.foodordermanagementservice.repository;


import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.domain.Order;
import org.sparta.foodordermanagementservice.dto.SelectOrderListDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
public class OrderDbTransactionalFacade {

    private final OrderRepository orderRepository;

    //    private final PaymentRepository paymentRepository;
    public List<Order> selectOrderList(SelectOrderListDto dto) {
        return orderRepository.selectOrderList(dto);
    }

}
