package org.sparta.foodordermanagementservice.common.utils;

import org.sparta.foodordermanagementservice.entity.enumerate.PaymentStatus;

public class PaymentStatusConverter extends QueryStringEnumConverter<PaymentStatus> {
    public PaymentStatusConverter() {
        super(PaymentStatus.class);
    }

}
