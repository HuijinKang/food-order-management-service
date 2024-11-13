package org.sparta.foodordermanagementservice.controller;

import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.common.ApiResponse;
import org.sparta.foodordermanagementservice.dto.UserDTO;
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

    @DeleteMapping("/{username}")
    @PutMapping("/{username}")
    public ApiResponse<Void> updateUser(@PathVariable("username") String username,
                                           @AuthenticationPrincipal UserDetails userDetails) {

        return ApiResponse.ofSuccess(null);
    }
}
