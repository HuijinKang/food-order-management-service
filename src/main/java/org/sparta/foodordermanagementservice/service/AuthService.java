package org.sparta.foodordermanagementservice.service;

import org.sparta.foodordermanagementservice.dto.request.SignupRequestDTO;

public interface AuthService {
    void signup(SignupRequestDTO signupRequestDTO);
}
