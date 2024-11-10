package org.sparta.foodordermanagementservice.dto.response;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.sparta.foodordermanagementservice.dto.PaymentDTO;
import org.sparta.foodordermanagementservice.entity.enumerate.PaymentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
@RequiredArgsConstructor
public class GetPaymentRes {

    UUID orderId;
    String receipt;
    PaymentStatus status;
    int payedPrice;
    LocalDateTime createdAt;
    String createdBy;
    LocalDateTime updatedAt;
    String updatedBy;
    LocalDateTime deletedAt;
    String deletedBy;

    public static GetPaymentRes from(PaymentDTO paymentDto) {

        return GetPaymentRes.builder()
                .orderId(paymentDto.getOrderId())
                .receipt(paymentDto.getReceipt())
                .status(paymentDto.getStatus())
                .payedPrice(paymentDto.getPayedPrice())
                .createdAt(paymentDto.getCreatedAt())
                .createdBy(paymentDto.getCreatedBy())
                .updatedAt(paymentDto.getUpdatedAt())
                .updatedBy(paymentDto.getUpdatedBy())
                .deletedAt(paymentDto.getDeletedAt())
                .deletedBy(paymentDto.getDeletedBy())
                .build();

    }
}