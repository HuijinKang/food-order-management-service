package org.sparta.foodordermanagementservice.controller;

import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.common.ApiResponse;
import org.sparta.foodordermanagementservice.dto.UserDTO;
import org.sparta.foodordermanagementservice.entity.UserRole;
import org.sparta.foodordermanagementservice.service.UserService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
