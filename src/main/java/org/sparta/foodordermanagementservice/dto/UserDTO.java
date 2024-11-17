package org.sparta.foodordermanagementservice.dto;

import lombok.Builder;
import lombok.Getter;
import org.sparta.foodordermanagementservice.entity.UserRole;
import org.sparta.foodordermanagementservice.entity.UserStatus;

@Getter
public class UserDTO {
    private final String username;
    private final UserStatus status;
    private final String nickname;
    private final String email;
    private final UserRole userRole;
    private final Boolean isPublic;

    @Builder
    public UserDTO(String username, UserStatus status, String nickname, String email, UserRole userRole, Boolean isPublic) {
        this.username = username;
        this.status = status;
        this.nickname = nickname;
        this.email = email;
        this.userRole = userRole;
        this.isPublic = isPublic;
    }
}
