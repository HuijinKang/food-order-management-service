package org.sparta.foodordermanagementservice.dto;


import lombok.Value;
import org.sparta.foodordermanagementservice.entity.enumerate.PaymentStatus;



@Value
public class ReqPatchPayment {

    PaymentStatus status;
    int payedPrice;
    String receipt;
}
