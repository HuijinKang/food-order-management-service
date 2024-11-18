package org.sparta.foodordermanagementservice.dto;

import lombok.Builder;
import lombok.Value;
import org.sparta.foodordermanagementservice.dto.request.ReqPostPayment;
import org.sparta.foodordermanagementservice.entity.Order;
import org.sparta.foodordermanagementservice.entity.Payment;
import org.sparta.foodordermanagementservice.entity.User;
import org.sparta.foodordermanagementservice.entity.enumerate.PaymentStatus;

import java.util.UUID;

@Value
@Builder
public class CreatePaymentDTO {

    UUID orderId;
    String username;
    PaymentStatus status;
    int payedPrice;
    String receipt;

    public static CreatePaymentDTO from(ReqPostPayment request) {

        return CreatePaymentDTO.builder()
                .orderId(request.getOrderId())
                .username(request.getUsername())
                .status(request.getStatus())
                .payedPrice(request.getPayedPrice())
                .receipt(request.getReceipt())
                .build();
    }

    public Payment toEntity(Order order, User user) {

        return Payment.builder()
                .order(order)
                .user(user)
                .status(this.status)
                .payedPrice(this.payedPrice)
                .receipt(this.receipt)
                .build();
    }
}
