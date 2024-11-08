package org.sparta.foodordermanagementservice.common.utils;

import lombok.NonNull;
import org.sparta.foodordermanagementservice.dto.request.SortedBy;
import org.springframework.core.convert.converter.Converter;

public class SortedByConverter implements Converter<String, SortedBy> {
    @Override
    public SortedBy convert(@NonNull String requested) {
        return SortedBy.of(requested);
    }
}
