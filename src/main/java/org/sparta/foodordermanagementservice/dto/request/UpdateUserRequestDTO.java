package org.sparta.foodordermanagementservice.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdateUserRequestDTO {
    private final String nickname;
    private final String password;
    private final Boolean isPublic;
    private final String email;

    @Builder
    public UpdateUserRequestDTO(String nickname, String password, Boolean isPublic, String email) {
        this.nickname = nickname;
        this.password = password;
        this.isPublic = isPublic;
        this.email = email;
    }
}
