package org.sparta.foodordermanagementservice.service;

import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.dto.PaymentDTO;
import org.sparta.foodordermanagementservice.dto.CreatePaymentDTO;
import org.sparta.foodordermanagementservice.dto.response.GetPaymentRes;
import org.sparta.foodordermanagementservice.entity.Payment;
import org.sparta.foodordermanagementservice.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository repository;

    @Override
    public GetPaymentRes readPayment(UUID paymentId) {

        PaymentDTO foundPayment
                = PaymentDTO.from(repository.readPayment(paymentId));

        return GetPaymentRes.from(foundPayment);
    }

    @Override
    public UUID createPayment(CreatePaymentDTO dto) {

        Payment createdPayment
                = repository.createPayment(dto);

        return createdPayment.getId();
    }

    @Override
    public void deletePayment(UUID paymentId) {

        String deleter = "admin";/*todo auth완료시 username으로 넣기 */

        repository.deletePayment(paymentId, deleter);
    }
}
