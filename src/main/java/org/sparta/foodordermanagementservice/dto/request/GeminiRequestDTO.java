package org.sparta.foodordermanagementservice.dto.request;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeminiRequestDTO {
    private List<Content> contents;

    @Getter @Setter
    public static class Content {
        private Parts parts;
    }

    @Getter @Setter
    public static class Parts {
        private String text;

    }

    @Builder
    public GeminiRequestDTO(String question) {
        this.contents = new ArrayList<>();
        Content content = new Content();
        Parts parts = new Parts();

        parts.setText(question);
        content.setParts(parts);

        this.contents.add(content);

    }
}