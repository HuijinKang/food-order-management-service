package org.sparta.foodordermanagementservice.service;

import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.dto.PaymentDTO;
import org.sparta.foodordermanagementservice.dto.CreatePaymentDTO;
import org.sparta.foodordermanagementservice.repository.PaymentTransactionFacade;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentTransactionFacade transaction;

    @Override
    public PaymentDTO readPayment(UUID paymentId) {

        return transaction.readPayment(paymentId);
    }

    @Override
    public PaymentDTO createPayment(CreatePaymentDTO dto) {

        return transaction.createPayment(dto);

    }

    @Override
    public void deletePayment(UUID paymentId) {

        String deleter = "admin";/*todo auth완료시 username으로 넣기 */

        transaction.deletePayment(paymentId, deleter);
    }
}
