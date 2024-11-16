package org.sparta.foodordermanagementservice.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdateUserRequestDTO {
    @NotNull(message = "닉네임 입력은 필수입니다.")
    private final String nickname;

    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+]{8,15}$",
            message = "패스워드는 8자 이상 15자 이하로 입력해야 하며, 알파벳 대소문자, 숫자, 특수문자만 포함할 수 있습니다.")
    private final String password;

    @NotNull(message = "정보 공개 여부는 필수입니다.")
    private final Boolean isPublic;

    @Email
    @NotNull(message = "이메일 입력은 필수입니다.")
    private final String email;

    @Builder
    @JsonCreator
    public UpdateUserRequestDTO(String nickname, String password, Boolean isPublic, String email) {
        this.nickname = nickname;
        this.password = password;
        this.isPublic = isPublic;
        this.email = email;
    }
}
