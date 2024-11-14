package org.sparta.foodordermanagementservice.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MenuDescriptionGenerateRequestDTO {
    @NotNull
    private final String question;

    @Builder
    @JsonCreator
    public MenuDescriptionGenerateRequestDTO(String question) {
        this.question = question;
    }
}
