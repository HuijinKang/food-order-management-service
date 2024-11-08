package org.sparta.foodordermanagementservice.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.common.utils.QueryStringEnumConverter;

@Getter
@RequiredArgsConstructor
public enum OrderListRequestCondition implements BaseQueryStringEnum {

    STORE_ID("storeId"),
    USER_NAME("userName");

    private final String queryString;

    public static class Converter
            extends QueryStringEnumConverter<OrderListRequestCondition> {
        public Converter() {
            super(OrderListRequestCondition.class);
        }
    }
}

