package org.sparta.foodordermanagementservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.common.ApiResponse;
import org.sparta.foodordermanagementservice.common.exeption.CustomException;
import org.sparta.foodordermanagementservice.common.exeption.ErrorCode;
import org.sparta.foodordermanagementservice.dto.request.SignupRequestDTO;
import org.sparta.foodordermanagementservice.entity.UserRole;
import org.sparta.foodordermanagementservice.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auths")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ApiResponse<Void> signup(@Valid @RequestBody SignupRequestDTO request) {
        UserRole userRole = request.getUserRole();

        if(userRole != UserRole.CUSTOMER && userRole != UserRole.OWNER) {
            throw new CustomException(ErrorCode.WRONG_ROLE);
        }

        authService.signup(request);

        return ApiResponse.ofSuccess(null);
    }

}
