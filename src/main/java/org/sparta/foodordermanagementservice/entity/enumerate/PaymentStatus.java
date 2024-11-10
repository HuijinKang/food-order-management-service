package org.sparta.foodordermanagementservice.entity.enumerate;

@SuppressWarnings("unused")
public enum PaymentStatus {

    PAY_WAIT,
    PAYED,
    PAY_PG_API_ERROR,
    PAY_PG_DENIED,

    CANCEL_WAIT,
    CANCELED,
    CANCEL_PG_API_ERROR,
    CANCEL_PG_DENIED

}
