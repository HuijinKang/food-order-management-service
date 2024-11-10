package org.sparta.foodordermanagementservice.service;

import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.dto.PaymentDTO;
import org.sparta.foodordermanagementservice.dto.PostPaymentDTO;
import org.sparta.foodordermanagementservice.repository.PaymentTransactionFacade;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentTransactionFacade transaction;

    @Override
    public PaymentDTO getPayment(UUID paymentId) {

        return transaction.selectPayment(paymentId);
    }

    @Override
    public PaymentDTO postPayment(PostPaymentDTO dto) {

        return transaction.insertPayment(dto);

    }

    @Override
    public void deletePayment(UUID paymentId) {

        String deleter = "admin";/*todo auth완료시 username으로 넣기 */

        transaction.deletePayment(paymentId, deleter);
    }
}
