package org.sparta.foodordermanagementservice.dto.request;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.sparta.foodordermanagementservice.entity.enumerate.PaymentStatus;

import java.util.UUID;

@Value
@Builder
@RequiredArgsConstructor
public class PostPaymentReq {

    UUID orderId;
    String username;
    PaymentStatus status;
    int payedPrice;
    String receipt;

}
