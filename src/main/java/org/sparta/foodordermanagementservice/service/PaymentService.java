package org.sparta.foodordermanagementservice.service;

import org.sparta.foodordermanagementservice.dto.PaymentDTO;
import org.sparta.foodordermanagementservice.dto.CreatePaymentDTO;
import org.sparta.foodordermanagementservice.dto.PaginatePaymentsDTO;
import org.sparta.foodordermanagementservice.dto.UpdatePaymentDTO;
import org.sparta.foodordermanagementservice.dto.response.ResPayment;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface PaymentService {
    ResPayment readPayment(UUID paymentId, UserDetails userDetails);

    UUID createPayment(CreatePaymentDTO dto);

    void deletePayment(UUID id);

    void updatePayment(UUID id, UpdatePaymentDTO dto);

    Page<ResPayment> paginatePayments(PaginatePaymentsDTO dto);

    boolean authorizeToRead(UserDetails userDetails, String paymentUserName, Boolean includingDeleted);
}
