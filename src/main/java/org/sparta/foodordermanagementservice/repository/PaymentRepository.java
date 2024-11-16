package org.sparta.foodordermanagementservice.repository;

import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.dto.CreatePaymentDTO;
import org.sparta.foodordermanagementservice.dto.UpdatePaymentDTO;
import org.sparta.foodordermanagementservice.entity.Order;
import org.sparta.foodordermanagementservice.entity.Payment;
import org.sparta.foodordermanagementservice.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PaymentRepository {

    private final PaymentDAO paymentDAO;
    //    private final UserDAO userDAO;
    private final OrderDAO orderDAO;

    @Transactional(readOnly = true)
    public Payment readPayment(UUID paymentId) {

        return paymentDAO.readPayment(paymentId);
    }

    @Transactional
    public Payment createPayment(CreatePaymentDTO dto) {
        //todo user완료되면 추후 수정
        User relatedUser
                = new User(); //userRepo.findByUsername(username);

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
}
