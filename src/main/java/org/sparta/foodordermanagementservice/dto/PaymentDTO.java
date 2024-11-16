package org.sparta.foodordermanagementservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.sparta.foodordermanagementservice.entity.Payment;
import org.sparta.foodordermanagementservice.entity.enumerate.PaymentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j


@AllArgsConstructor
@Builder
public class PaymentDTO {

    private UUID id;
    private UUID orderId;
    private String receipt;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private LocalDateTime deletedAt;
    private String deletedBy;
    private PaymentStatus status;
    private int payedPrice;


    public static PaymentDTO from (Payment payment) {

        return PaymentDTO.builder()
                .id(payment.getId())
                .orderId(payment.getOrder().getId())
                .receipt(payment.getReceipt())
                .createdAt(payment.getCreatedAt())
                .createdBy(payment.getCreatedBy())
                .updatedAt(payment.getUpdatedAt())
                .updatedBy(payment.getUpdatedBy())
                .deletedAt(payment.getDeletedAt())
                .deletedBy(payment.getDeletedBy())
                .status(payment.getStatus())
                .payedPrice(payment.getPayedPrice())
                .build();
    }
}
