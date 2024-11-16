package org.sparta.foodordermanagementservice.entity.enumerate;

import lombok.AllArgsConstructor;
import org.sparta.foodordermanagementservice.dto.request.BaseQueryStringEnum;

@SuppressWarnings("unused")
@AllArgsConstructor
public enum OrderType implements BaseQueryStringEnum {

    DELIVERY("delivery" ),
    PACKAGING("packaging");


    private final String queryString;

    @Override
    public String getQueryString() {
        return queryString;
    }
}
