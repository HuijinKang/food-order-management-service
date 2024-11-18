package org.sparta.foodordermanagementservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.sparta.foodordermanagementservice.entity.Payment;
import org.sparta.foodordermanagementservice.entity.enumerate.PaymentStatus;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
public class PaymentDTO {

    private final UUID id;
    private final UUID orderId;
    private final long userId;
    private String receipt;
    @Setter
    private PaymentStatus status;
    private int payedPrice;
    private final LocalDateTime createdAt;
    private final String createdBy;
    private final LocalDateTime updatedAt;
    private final String updatedBy;
    private LocalDateTime deletedAt;
    private String deletedBy;





    public static PaymentDTO from(Payment payment) {

        return PaymentDTO.builder()
                .id(payment.getId())
                .orderId(payment.getOrder().getId())
                .userId(payment.getUser().getId())
                .receipt(payment.getReceipt())
                .status(payment.getStatus())
                .payedPrice(payment.getPayedPrice())
                .createdAt(payment.getCreatedAt())
                .createdBy(payment.getCreatedBy())
                .updatedAt(payment.getUpdatedAt())
                .updatedBy(payment.getUpdatedBy())
                .deletedAt(payment.getDeletedAt())
                .deletedBy(payment.getDeletedBy())
                .build();
    }

    @Builder
    public PaymentDTO(UUID id,
                      UUID orderId,
                      long userId,
                      String receipt,
                      PaymentStatus status,
                      int payedPrice,
                      LocalDateTime createdAt,
                      String createdBy,
                      LocalDateTime updatedAt,
                      String updatedBy,
                      LocalDateTime deletedAt,
                      String deletedBy
    ) {
        this.id = id;
        this.orderId = orderId;
        this.userId = userId;
        this.receipt = receipt;
        this.status = status;
        this.payedPrice = payedPrice;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
        this.deletedAt = deletedAt;
        this.deletedBy = deletedBy;
    }
}
