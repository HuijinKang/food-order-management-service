package org.sparta.foodordermanagementservice.dto;

import lombok.Builder;
import lombok.Getter;
import org.sparta.foodordermanagementservice.entity.UserRole;

@Getter
public class LoginUser{
    private final String username;
    private final String nickname;
    private final String email;
    private final Boolean isPublic;
    private final UserRole userRole;

    @Builder
    public LoginUser(String username, String nickname, String email, Boolean isPublic, UserRole userRole) {
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.isPublic = isPublic;
        this.userRole = userRole;
    }
}
