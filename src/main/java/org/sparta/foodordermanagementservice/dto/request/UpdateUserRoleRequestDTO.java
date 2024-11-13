package org.sparta.foodordermanagementservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.sparta.foodordermanagementservice.entity.UserRole;

@Getter
public class UpdateUserRoleRequestDTO {
    @NotNull(message = "역할 입력은 필수입니다.")
    private final UserRole userRole;

    @Builder
    public UpdateUserRoleRequestDTO(@NotNull UserRole userRole) {
        this.userRole = userRole;
    }
}
