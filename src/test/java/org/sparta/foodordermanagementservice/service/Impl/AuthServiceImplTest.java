package org.sparta.foodordermanagementservice.service.Impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sparta.foodordermanagementservice.common.exeption.CustomException;
import org.sparta.foodordermanagementservice.common.exeption.ErrorCode;
import org.sparta.foodordermanagementservice.dto.request.SignupRequestDTO;
import org.sparta.foodordermanagementservice.entity.User;
import org.sparta.foodordermanagementservice.entity.UserRole;
import org.sparta.foodordermanagementservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    private SignupRequestDTO validSignUpRequest;

    @BeforeEach
    void setUp() {
        validSignUpRequest = SignupRequestDTO.builder()
                .username("testuser")
                .password("testpassword")
                .email("test@email.com")
                .nickname("testnickname")
                .isPublic(true)
                .userRole(UserRole.CUSTOMER)
                .build();
    }

    @Test
    @DisplayName("회원가입 성공")
    void signupSuccess() {

        when(userRepository.existsByUsername(validSignUpRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByNickname(validSignUpRequest.getNickname())).thenReturn(false);
        when(userRepository.existsByEmail(validSignUpRequest.getEmail())).thenReturn(false);

        authService.signup(validSignUpRequest);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("회원가입 실패 - username 중복")
    void signupFailWhenUsernameIsDuplicate() {
        SignupRequestDTO duplicatedUser = new SignupRequestDTO();
        BeanUtils.copyProperties(validSignUpRequest, duplicatedUser);

        when(userRepository.existsByUsername(validSignUpRequest.getUsername())).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, () -> authService.signup(duplicatedUser));

        assertEquals(ErrorCode.DUPLICATE_USERNAME, exception.getErrorCode());
        verify(userRepository, times(1)).existsByUsername(anyString());
    }

    @Test
    @DisplayName("회원가입 실패 - nickname 중복")
    void signupFailWhenNicknameIsDuplicate() {
        SignupRequestDTO duplicatedUser = new SignupRequestDTO();
        BeanUtils.copyProperties(validSignUpRequest, duplicatedUser);

        when(userRepository.existsByNickname(validSignUpRequest.getNickname())).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, () -> authService.signup(duplicatedUser));

        assertEquals(ErrorCode.DUPLICATE_NICKNAME, exception.getErrorCode());
        verify(userRepository, times(1)).existsByNickname(anyString());

    }

    @Test
    @DisplayName("회원가입 실패 - email 중복")
    void signupFailWhenEmailIsDuplicate() {
        SignupRequestDTO duplicatedUser = new SignupRequestDTO();
        BeanUtils.copyProperties(validSignUpRequest, duplicatedUser);

        when(userRepository.existsByEmail(validSignUpRequest.getEmail())).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, () -> authService.signup(duplicatedUser));

        assertEquals(ErrorCode.DUPLICATE_EMAIL, exception.getErrorCode());
        verify(userRepository, times(1)).existsByEmail(anyString());

    }
}