package org.sparta.foodordermanagementservice.dto.response;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

public class SortedByConverter implements Converter<String, SortedBy> {
    @Override
    public SortedBy convert(@NonNull String requested) {
        return SortedBy.of(requested);
    }
}
