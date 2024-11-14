package org.sparta.foodordermanagementservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.common.ApiResponse;
import org.sparta.foodordermanagementservice.common.exeption.CustomException;
import org.sparta.foodordermanagementservice.common.exeption.ErrorCode;
import org.sparta.foodordermanagementservice.dto.UserDTO;
import org.sparta.foodordermanagementservice.dto.request.UpdateUserRequestDTO;
import org.sparta.foodordermanagementservice.dto.request.UpdateUserRoleRequestDTO;
import org.sparta.foodordermanagementservice.entity.UserRole;
import org.sparta.foodordermanagementservice.service.UserService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{username}")
    @Secured({UserRole.Authority.MANAGER, UserRole.Authority.MASTER})
    public ApiResponse<UserDTO> getUser(@PathVariable("username") String username) {
        UserDTO userDTO = userService.getUser(username);

        return ApiResponse.ofSuccess(userDTO);
    }

    @PutMapping("/{username}")
    public ApiResponse<UserDTO> updateUser(@PathVariable("username") String username,
                                           @Valid @RequestBody UpdateUserRequestDTO request,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        if(userDetails.getUsername().equals(username) ||
                hasManagerRole(userDetails) ||
                hasMasterRole(userDetails)
        ) {
            userService.updateUser(username, request);
        } else {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        return ApiResponse.ofSuccess(null);
    }

    private static boolean hasMasterRole(UserDetails userDetails) {
        return userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(UserRole.Authority.MANAGER));
    }

    private static boolean hasManagerRole(UserDetails userDetails) {
        return userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(UserRole.Authority.MANAGER));
    }


    @PatchMapping("/role/{username}")
    @Secured(UserRole.Authority.MASTER)
    public ApiResponse<Void> updateUserRole(@PathVariable("username") String username,
                                            @Valid @RequestBody UpdateUserRoleRequestDTO request) {

        UserRole role = request.getUserRole();

        userService.updateUserRole(username, role);

        return ApiResponse.ofSuccess(null);
    }
}
