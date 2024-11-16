package org.sparta.foodordermanagementservice.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.common.exeption.CustomException;
import org.sparta.foodordermanagementservice.common.exeption.ErrorCode;
import org.sparta.foodordermanagementservice.dto.PaginatePaymentsDTO;
import org.sparta.foodordermanagementservice.dto.UpdatePaymentDTO;
import org.sparta.foodordermanagementservice.entity.Payment;
import org.sparta.foodordermanagementservice.entity.QPayment;
import org.sparta.foodordermanagementservice.entity.enumerate.OrderSpec;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PaymentDAO {

    private final PaymentJpaRepository jpaRepo;
    private final JPAQueryFactory queryFactory;
    private final QPayment payment = QPayment.payment;

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

    public void update(UUID id, UpdatePaymentDTO dto) {

        Payment target = jpaRepo.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RESOURCE));

        target.setStatus(dto.getStatus());
        target.setPayedPrice(dto.getPayedPrice());
        target.setReceipt(dto.getReceipt());

        jpaRepo.save(target);
    }

    public List<Payment> readCurrentPage(PaginatePaymentsDTO dto) {

        return queryFactory
                .selectFrom(payment)
                .where(
                        payment.user.username.eq(dto.getUsername()),
                        dto.isIncludingDeleted()
                                ? null
                                : payment.deletedAt.isNull()
                )
                .orderBy(OrderSpec.of(dto.getSortedBy(), dto.isAsc()))
                .offset(dto.getPageSize() * dto.getPageNumber())
                .limit(dto.getPageSize())
                .fetch();
    }

    public long countTotal(String username, boolean includingDeleted) {

        return queryFactory
                .selectFrom(payment)
                .where(
                        payment.user.username.eq(username),
                        includingDeleted
                                ? null
                                : payment.deletedAt.isNull()
                )
                .fetch()
                .size();

    }
}
