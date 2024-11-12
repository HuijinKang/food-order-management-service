package org.sparta.foodordermanagementservice.service;

import org.sparta.foodordermanagementservice.dto.PaymentDTO;
import org.sparta.foodordermanagementservice.dto.CreatePaymentDTO;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface PaymentService {
    PaymentDTO readPayment(UUID paymentId);

    PaymentDTO createPayment(CreatePaymentDTO dto);

    void deletePayment(UUID id);
}
