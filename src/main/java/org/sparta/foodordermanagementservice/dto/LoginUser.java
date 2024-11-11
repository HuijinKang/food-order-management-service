package org.sparta.foodordermanagementservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.foodordermanagementservice.entity.UserRole;

@Getter
@NoArgsConstructor
public class LoginUser{
    private String username;
    private String nickname;
    private String email;
    private Boolean isPublic;
    private UserRole userRole;

    @Builder
    public LoginUser(String username, String nickname, String email, Boolean isPublic, UserRole userRole) {
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.isPublic = isPublic;
        this.userRole = userRole;
    }
}
