package org.sparta.foodordermanagementservice.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.common.exeption.CustomException;
import org.sparta.foodordermanagementservice.common.exeption.ErrorCode;
import org.sparta.foodordermanagementservice.entity.Payment;
import org.sparta.foodordermanagementservice.entity.QPayment;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PaymentDAO {

    private final PaymentJpaRepository jpaRepo;
    private final JPAQueryFactory queryFactory;
    private final QPayment qPayment = QPayment.payment;

    public Payment readPayment(UUID paymentId) {

        return jpaRepo.findById(paymentId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RESOURCE));
    }


    public Payment createPayment(Payment toSave) {
        return jpaRepo.save(toSave);
    }

    public void softDeletePayment(UUID paymentId, String deletedBy) {

        Payment target = jpaRepo.findById(paymentId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RESOURCE));

        target.setDeletedAt(LocalDateTime.now());
        target.setDeletedBy(deletedBy);

        jpaRepo.save(target);
    }
}
