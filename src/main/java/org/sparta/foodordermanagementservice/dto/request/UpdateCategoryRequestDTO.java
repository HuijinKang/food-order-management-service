package org.sparta.foodordermanagementservice.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter // 테스트시 필요
public class UpdateCategoryRequestDTO {
    private String name;
}
