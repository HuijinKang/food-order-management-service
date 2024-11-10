package org.sparta.foodordermanagementservice.repository;

import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.dto.PaymentDTO;
import org.sparta.foodordermanagementservice.dto.PostPaymentDTO;
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

    public PaymentDTO selectPayment(UUID paymentId) {

        Payment selected = paymentRepo.selectPayment(paymentId);

        return PaymentDTO.from(selected);
    }

    public PaymentDTO insertPayment(PostPaymentDTO dto) {
        //todo 테스트용, 추후 삭제
        User relatedUser = new User();

        Order relatedOrder = orderRepo.selectOrder(dto.getOrderId());

        Payment toSave = dto.toEntity(relatedOrder, relatedUser);

        Payment saved = paymentRepo.insertPayment(toSave);

        return PaymentDTO.from(saved);
    }

    public void deletePayment(UUID paymentId, String deletedBy) {

        paymentRepo.deletePayment(paymentId, deletedBy);
    }
}
