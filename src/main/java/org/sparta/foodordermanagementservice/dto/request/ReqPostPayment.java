package org.sparta.foodordermanagementservice.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.sparta.foodordermanagementservice.common.utils.PaymentStatusConverter;
import org.sparta.foodordermanagementservice.entity.enumerate.PaymentStatus;

import java.util.UUID;

@Value
@Builder
@RequiredArgsConstructor
public class ReqPostPayment {

    UUID orderId;
    String username;
    PaymentStatus status;
    int payedPrice;
    String receipt;

    private static final PaymentStatusConverter paymentStatusConverter = new PaymentStatusConverter();

    @JsonCreator
    public ReqPostPayment(UUID orderId, String username, String status, int payedPrice, String receipt) {
        this.orderId = orderId;
        this.username = username;
        this.status = paymentStatusConverter.convert(status);
        this.payedPrice = payedPrice;
        this.receipt = receipt;
    }

}
