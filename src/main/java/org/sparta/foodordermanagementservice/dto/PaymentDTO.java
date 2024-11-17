package org.sparta.foodordermanagementservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.sparta.foodordermanagementservice.entity.Payment;
import org.sparta.foodordermanagementservice.entity.enumerate.PaymentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j

@Getter
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

        log.info(payment.toString());

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


    public PaymentDTO(UUID id,
                      UUID orderId,
                      String receipt,
                      LocalDateTime createdAt,
                      String createdBy,
                      LocalDateTime updatedAt,
                      String updatedBy,
                      LocalDateTime deletedAt,
                      String deletedBy,
                      PaymentStatus status,
                      int payedPrice) {
        this.id = id;
        this.orderId = orderId;
        this.receipt = receipt;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
        this.deletedAt = deletedAt;
        this.deletedBy = deletedBy;
        this.status = status;
        this.payedPrice = payedPrice;
    }
}
