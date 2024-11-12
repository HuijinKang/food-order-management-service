package org.sparta.foodordermanagementservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.foodordermanagementservice.dto.LoginUser;

@Getter
public class LoginResponseDTO {
    private final String jwtToken;
    private final LoginUser user;

    @Builder
    public LoginResponseDTO(String jwtToken, LoginUser user) {
        this.jwtToken = jwtToken;
        this.user = user;
    }
}
