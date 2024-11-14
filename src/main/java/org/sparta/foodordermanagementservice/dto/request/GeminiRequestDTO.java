package org.sparta.foodordermanagementservice.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Collections;
import java.util.List;

@Getter
public class GeminiRequestDTO {

    private final List<Content> contents;

    @JsonCreator
    public GeminiRequestDTO(@JsonProperty("contents") List<Content> contents) {
        this.contents = contents;
    }

    @Getter
    public static class Content {
        private final Parts parts;

        @JsonCreator
        public Content(@JsonProperty("parts") Parts parts) {
            this.parts = parts;
        }
    }

    @Getter
    public static class Parts {
        private final String text;

        // Parts 클래스의 생성자
        @JsonCreator
        public Parts(@JsonProperty("text") String text) {
            this.text = text;
        }
    }

    @Builder
    public static GeminiRequestDTO createGeminiRequestDTO(String question) {
        Parts parts = new Parts(question);
        Content content = new Content(parts);
        return new GeminiRequestDTO(Collections.singletonList(content));
    }
}