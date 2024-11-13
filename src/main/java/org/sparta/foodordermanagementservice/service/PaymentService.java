package org.sparta.foodordermanagementservice.service;

import org.sparta.foodordermanagementservice.dto.CreatePaymentDTO;
import org.sparta.foodordermanagementservice.dto.response.GetPaymentRes;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface PaymentService {
    GetPaymentRes readPayment(UUID paymentId);

    UUID createPayment(CreatePaymentDTO dto);

    void deletePayment(UUID id);
}
