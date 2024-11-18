package org.sparta.foodordermanagementservice.entity.enumerate;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.dto.request.BaseQueryStringEnum;

@SuppressWarnings("unused")
@RequiredArgsConstructor
public enum OrderStatus implements BaseQueryStringEnum {
    WAIT("wait"),
    ACCEPT("accept"),
    REJECT("reject"),
    CUSTOMER_CANCEL("customerCancel"),
//    STORE_CANCEL("storeCancel"),
    COMPLETE("complete");

    private final String queryString;

    @Override
    @JsonValue
    public String getQueryString() {
        return queryString;
    }
}
