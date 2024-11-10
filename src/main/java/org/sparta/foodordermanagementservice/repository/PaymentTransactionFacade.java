package org.sparta.foodordermanagementservice.repository;

import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.dto.PaymentDTO;
import org.sparta.foodordermanagementservice.entity.Payment;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
@Transactional
@RequiredArgsConstructor
public class PaymentTransactionFacade {

    private final PaymentRepository paymentRepo;

    public PaymentDTO selectPayment(UUID paymentId) {

        Payment selected = paymentRepo.selectPayment(paymentId);

        return PaymentDTO.from(selected);
    }
}
