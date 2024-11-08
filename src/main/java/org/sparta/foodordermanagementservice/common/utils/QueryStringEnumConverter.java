package org.sparta.foodordermanagementservice.common.utils;

import lombok.NonNull;
import org.sparta.foodordermanagementservice.common.exeption.CustomException;
import org.sparta.foodordermanagementservice.common.exeption.ErrorCode;
import org.sparta.foodordermanagementservice.dto.request.BaseQueryStringEnum;
import org.springframework.core.convert.converter.Converter;

import java.util.Arrays;

public abstract class QueryStringEnumConverter<E extends Enum<E> & BaseQueryStringEnum>
        implements Converter<String, E> {

    private final Class<E> enumClazz;

    protected QueryStringEnumConverter(Class<E> enumClazz) {
        this.enumClazz = enumClazz;
    }

    @Override
    public final E convert(@NonNull String queryString) {

        E convertedEnum
                = Arrays.stream(enumClazz.getEnumConstants())
                .filter(e -> e.getQueryString().equals(queryString))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST));

        return convertedEnum;
    }

}
