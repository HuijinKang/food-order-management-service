package org.sparta.foodordermanagementservice.repository;

import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.common.exeption.CustomException;
import org.sparta.foodordermanagementservice.common.exeption.ErrorCode;
import org.sparta.foodordermanagementservice.dto.SelectOrderListDTO;
import org.sparta.foodordermanagementservice.entity.Order;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Repository
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class OrderRepository {

    private final OrderJpaRepository orderJpaRepo;
    private final OrderQDslRepository orderQDslRepo;

    public List<Order> readOrderList(SelectOrderListDTO dto) {

        return orderQDslRepo.readOrderList(dto);
    }

    public Order readOrder(UUID orderId) {
        return orderJpaRepo.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RESOURCE));
    }

    public void softDeleteOrder(UUID orderId) {

        Order order = orderJpaRepo.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RESOURCE));

        order.setDeletedAt(LocalDateTime.now());
        order.setDeletedBy("system");//todo auth에서 로그인아이디 받아오도록 수정

        orderJpaRepo.save(order);
    }

}
