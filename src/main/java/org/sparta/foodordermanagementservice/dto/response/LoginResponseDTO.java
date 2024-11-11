package org.sparta.foodordermanagementservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.foodordermanagementservice.dto.LoginUser;

@Getter
@NoArgsConstructor
public class LoginResponseDTO {
    private String jwtToken;
    private LoginUser user;

    @Builder
    public LoginResponseDTO(String jwtToken, LoginUser user) {
        this.jwtToken = jwtToken;
        this.user = user;
    }
}
