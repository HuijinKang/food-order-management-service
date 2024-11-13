package org.sparta.foodordermanagementservice.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginRequestDTO {
    @NotNull(message = "아이디 입력은 필수입니다.")
    @Pattern(regexp = "^[a-z0-9]{4,10}$", message = "아이디는 소문자 알파벳과 숫자로 구성된 4자 이상 10자 이하여야 합니다.")
    private final String username;

    @NotNull(message = "패스워드 입력은 필수입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+]{8,15}$",
            message = "패스워드는 8자 이상 15자 이하로 입력해야 하며, 알파벳 대소문자, 숫자, 특수문자만 포함할 수 있습니다.")
    private final String password;

    @Builder
    public LoginRequestDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
