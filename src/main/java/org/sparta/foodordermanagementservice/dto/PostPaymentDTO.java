package org.sparta.foodordermanagementservice.dto;

import lombok.Builder;
import lombok.Value;
import org.sparta.foodordermanagementservice.dto.request.PostPaymentReq;
import org.sparta.foodordermanagementservice.entity.Order;
import org.sparta.foodordermanagementservice.entity.Payment;
import org.sparta.foodordermanagementservice.entity.User;
import org.sparta.foodordermanagementservice.entity.enumerate.PaymentStatus;

import java.util.UUID;

@Value
@Builder
public class PostPaymentDTO {

    UUID orderId;
    String username;
    PaymentStatus status;
    int payedPrice;
    String receipt;

    public static PostPaymentDTO from(PostPaymentReq request) {

        return PostPaymentDTO.builder()
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
