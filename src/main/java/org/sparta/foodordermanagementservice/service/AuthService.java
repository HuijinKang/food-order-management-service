package org.sparta.foodordermanagementservice.service;

import org.sparta.foodordermanagementservice.dto.request.LoginRequestDTO;
import org.sparta.foodordermanagementservice.dto.request.SignupRequestDTO;
import org.sparta.foodordermanagementservice.dto.response.LoginResponseDTO;

public interface AuthService {
    void signup(SignupRequestDTO signupRequestDTO);

    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
}
