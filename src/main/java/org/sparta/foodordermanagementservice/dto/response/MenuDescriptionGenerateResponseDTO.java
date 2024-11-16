package org.sparta.foodordermanagementservice.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MenuDescriptionGenerateResponseDTO {
    private final String answer;

    @Builder
    public MenuDescriptionGenerateResponseDTO(String answer) {
        this.answer = answer;
    }
}
