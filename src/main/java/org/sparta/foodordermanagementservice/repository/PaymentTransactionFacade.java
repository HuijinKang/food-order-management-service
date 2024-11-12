package org.sparta.foodordermanagementservice.repository;

import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.dto.PaymentDTO;
import org.sparta.foodordermanagementservice.dto.CreatePaymentDTO;
import org.sparta.foodordermanagementservice.entity.Order;
import org.sparta.foodordermanagementservice.entity.Payment;
import org.sparta.foodordermanagementservice.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
@Transactional
@RequiredArgsConstructor
public class PaymentTransactionFacade {

    private final PaymentRepository paymentRepo;
    private final UserRepository userRepo;
    private final OrderRepository orderRepo;

    public PaymentDTO readPayment(UUID paymentId) {

        Payment readPayment
                = paymentRepo.readPayment(paymentId);

        return PaymentDTO.from(readPayment);
    }

    public PaymentDTO createPayment(CreatePaymentDTO dto) {
        //todo 테스트용, 추후 수정
        User relatedUser
                = new User(); //userRepo.findByUsername(username);

        Order relatedOrder
                = orderRepo.readOrder(dto.getOrderId());

        Payment createInfo
                = dto.toEntity(relatedOrder, relatedUser);

        Payment createdPayment
                = paymentRepo.createPayment(createInfo);

        return PaymentDTO.from(createdPayment);
    }

    public void deletePayment(UUID paymentId, String deletedBy) {

        paymentRepo.softDeletePayment(paymentId, deletedBy);
    }
}
