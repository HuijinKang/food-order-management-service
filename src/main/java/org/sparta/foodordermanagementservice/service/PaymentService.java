package org.sparta.foodordermanagementservice.service;

import org.sparta.foodordermanagementservice.dto.PaymentDTO;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface PaymentService {
    PaymentDTO getPayment(UUID paymentId);
}
