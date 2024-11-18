package org.sparta.foodordermanagementservice.entity.enumerate;

@SuppressWarnings("unused")
public enum PaymentStatus {

    PAY_WAIT,
    PAYED,
    PAY_PG_API_ERROR,
    PG_DENIED_PAY,

    CANCEL_WAIT,
    CANCELED,
    CANCEL_PG_API_ERROR,
    PG_DENIED_CANCEL

}
