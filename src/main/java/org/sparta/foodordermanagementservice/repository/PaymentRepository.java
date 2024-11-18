package org.sparta.foodordermanagementservice.repository;

import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.common.exeption.CustomException;
import org.sparta.foodordermanagementservice.common.exeption.ErrorCode;
import org.sparta.foodordermanagementservice.dto.CreatePaymentDTO;
import org.sparta.foodordermanagementservice.dto.PaginatePaymentsDTO;
import org.sparta.foodordermanagementservice.dto.PaymentDTO;
import org.sparta.foodordermanagementservice.dto.UpdatePaymentDTO;
import org.sparta.foodordermanagementservice.entity.Order;
import org.sparta.foodordermanagementservice.entity.Payment;
import org.sparta.foodordermanagementservice.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PaymentRepository {

    private final PaymentDAO paymentDAO;
    private final UserRepository userDAO;
    private final OrderDAO orderDAO;

    @Transactional(readOnly = true)
    public Payment readPayment(UUID paymentId) {

        return paymentDAO.readPayment(paymentId);
    }

    @Transactional(readOnly = true)
    public Payment readOrderPayment(UUID orderId) {

        return paymentDAO.readOrderPayment(orderId);
    }

    @Transactional
    public Payment createPayment(CreatePaymentDTO dto) {

        User relatedUser
                = userDAO.findByUsername(dto.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RESOURCE));

        Order relatedOrder
                = orderDAO.readOrder(dto.getOrderId());

        Payment createInfo
                = dto.toEntity(relatedOrder, relatedUser);

        return paymentDAO.createPayment(createInfo);
    }

    @Transactional
    public void deletePayment(UUID paymentId, String deletedBy) {

        paymentDAO.softDeletePayment(paymentId, deletedBy);
    }

    @Transactional
    public void updatePayment(UUID id, UpdatePaymentDTO dto) {

        paymentDAO.update(id, dto);
    }

    public List<PaymentDTO> readCurrentPagePayments(PaginatePaymentsDTO dto) {

        return paymentDAO.readCurrentPage(dto)
                .stream()
                .map(PaymentDTO::from)
                .toList();
    }

    public long countTotalPayments(String username, boolean includingDeleted) {

        return paymentDAO.countTotal(username, includingDeleted);

    }
}
