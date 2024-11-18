package org.sparta.foodordermanagementservice.entity.enumerate;

import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.dto.request.BaseQueryStringEnum;

@SuppressWarnings("unused")


@RequiredArgsConstructor
public enum PaymentStatus implements BaseQueryStringEnum {

    PAY_WAIT("payWait"),
    PAYED("payed"),
    PAY_PG_API_ERROR("payPgApiError"),
    PG_DENIED_PAY("pgDeniedPay"),

    CANCEL_WAIT("cancelWait"),
    CANCELED("canceled"),
    CANCEL_PG_API_ERROR("cancelPgApiError"),
    PG_DENIED_CANCEL("pgDeniedCancel"),

    OWNER_SETTLEMENT_WAIT("ownerSettlementWait"),
    OWNER_SETTLED("ownerSettled"),
    PG_DENIED_OWNER_SETTLEMENT("pgDeniedOwnerSettlement");


    private final String queryString;

    @Override
    public String getQueryString() {
        return queryString;
    }
}
