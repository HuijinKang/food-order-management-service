package org.sparta.foodordermanagementservice.dto;


import lombok.Builder;
import lombok.Getter;
import org.sparta.foodordermanagementservice.entity.enumerate.PaymentStatus;


@Getter
public class UpdatePaymentDTO {

    private PaymentStatus status;
    private int payedPrice;
    private String receipt;


    public static UpdatePaymentDTO from(ReqPatchPayment request) {

        return UpdatePaymentDTO.builder()
                .status(request.getStatus())
                .payedPrice(request.getPayedPrice())
                .receipt(request.getReceipt())
                .build();
    }


    @Builder
    public UpdatePaymentDTO(PaymentStatus status, int payedPrice, String receipt) {
        this.status = status;
        this.payedPrice = payedPrice;
        this.receipt = receipt;
    }
}
