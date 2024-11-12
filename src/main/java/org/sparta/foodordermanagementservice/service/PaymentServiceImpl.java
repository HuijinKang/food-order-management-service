package org.sparta.foodordermanagementservice.service;

import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.dto.PaymentDTO;
import org.sparta.foodordermanagementservice.dto.CreatePaymentDTO;
import org.sparta.foodordermanagementservice.repository.PaymentPersistenceFacade;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentPersistenceFacade persistence;

    @Override
    public PaymentDTO readPayment(UUID paymentId) {

        return persistence.readPayment(paymentId);
    }

    @Override
    public PaymentDTO createPayment(CreatePaymentDTO dto) {

        return persistence.createPayment(dto);

    }

    @Override
    public void deletePayment(UUID paymentId) {

        String deleter = "admin";/*todo auth완료시 username으로 넣기 */

        persistence.deletePayment(paymentId, deleter);
    }
}
