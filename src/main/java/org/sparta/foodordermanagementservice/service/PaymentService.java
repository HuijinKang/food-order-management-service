package org.sparta.foodordermanagementservice.service;

import org.sparta.foodordermanagementservice.dto.PaymentDTO;
import org.sparta.foodordermanagementservice.dto.PostPaymentDTO;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface PaymentService {
    PaymentDTO getPayment(UUID paymentId);

    PaymentDTO postPayment(PostPaymentDTO dto);

    void deletePayment(UUID id);
}
