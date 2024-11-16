package org.sparta.foodordermanagementservice.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.common.utils.QueryStringEnumConverter;

@Getter
@RequiredArgsConstructor
public enum PaginateOrdersReqCondition implements BaseQueryStringEnum {

    STORE_ID("storeId"),
    USER_NAME("username");

    private final String queryString;

    public static class Converter
            extends QueryStringEnumConverter<PaginateOrdersReqCondition> {
        public Converter() {
            super(PaginateOrdersReqCondition.class);
        }
    }
}

