package org.sparta.foodordermanagementservice.service;

import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.dto.PaymentDTO;
import org.sparta.foodordermanagementservice.dto.CreatePaymentDTO;
import org.sparta.foodordermanagementservice.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository repository;

    @Override
    public PaymentDTO readPayment(UUID paymentId) {

        return repository.readPayment(paymentId);
    }

    @Override
    public PaymentDTO createPayment(CreatePaymentDTO dto) {

        return repository.createPayment(dto);

    }

    @Override
    public void deletePayment(UUID paymentId) {

        String deleter = "admin";/*todo auth완료시 username으로 넣기 */

        repository.deletePayment(paymentId, deleter);
    }
}
