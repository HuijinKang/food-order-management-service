package org.sparta.foodordermanagementservice.service.Impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.sparta.foodordermanagementservice.common.exeption.CustomException;
import org.sparta.foodordermanagementservice.common.exeption.ErrorCode;
import org.sparta.foodordermanagementservice.dto.request.LoginRequestDTO;
import org.sparta.foodordermanagementservice.dto.request.SignupRequestDTO;
import org.sparta.foodordermanagementservice.dto.response.LoginResponseDTO;
import org.sparta.foodordermanagementservice.entity.User;
import org.sparta.foodordermanagementservice.entity.UserRole;
import org.sparta.foodordermanagementservice.entity.UserStatus;
import org.sparta.foodordermanagementservice.repository.UserRepository;
import org.sparta.foodordermanagementservice.security.JwtUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

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

    @Spy
    private ModelMapper modelMapper;

    @Mock
    private JwtUtil jwtUtil;

    private User testUser;
    private SignupRequestDTO validSignUpRequest;
    private LoginRequestDTO validLoginRequest;

    @BeforeEach
    void setUp() {
         testUser = User.builder()
                .username("testUser")
                .password("testPassword")
                .email("test@email.com")
                .nickname("testNickname")
                .isPublic(true)
                .userRole(UserRole.CUSTOMER)
                .createdBy("testUser")
                .updatedBy("testUser")
                .build();
        validSignUpRequest = SignupRequestDTO.builder()
                .username(testUser.getUsername())
                .password(testUser.getPassword())
                .email(testUser.getEmail())
                .nickname(testUser.getNickname())
                .isPublic(testUser.getIsPublic())
                .userRole(testUser.getUserRole())
                .build();
        validLoginRequest = LoginRequestDTO.builder()
                .username(testUser.getUsername())
                .password(testUser.getPassword())
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
        modelMapper.getConfiguration().setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true);
        SignupRequestDTO duplicatedUser = modelMapper.map(validSignUpRequest, SignupRequestDTO.class);

        when(userRepository.existsByUsername(validSignUpRequest.getUsername())).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, () -> authService.signup(duplicatedUser));

        assertEquals(ErrorCode.DUPLICATE_USERNAME, exception.getErrorCode());
        verify(userRepository, times(1)).existsByUsername(anyString());
    }

    @Test
    @DisplayName("회원가입 실패 - nickname 중복")
    void signupFailWhenNicknameIsDuplicate() {
        modelMapper.getConfiguration().setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true);
        SignupRequestDTO duplicatedUser = modelMapper.map(validSignUpRequest, SignupRequestDTO.class);

        when(userRepository.existsByNickname(validSignUpRequest.getNickname())).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, () -> authService.signup(duplicatedUser));

        assertEquals(ErrorCode.DUPLICATE_NICKNAME, exception.getErrorCode());
        verify(userRepository, times(1)).existsByNickname(anyString());

    }

    @Test
    @DisplayName("회원가입 실패 - email 중복")
    void signupFailWhenEmailIsDuplicate() {
        modelMapper.getConfiguration().setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                        .setFieldMatchingEnabled(true);
        SignupRequestDTO duplicatedUser = modelMapper.map(validSignUpRequest, SignupRequestDTO.class);

        when(userRepository.existsByEmail(validSignUpRequest.getEmail())).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, () -> authService.signup(duplicatedUser));

        assertEquals(ErrorCode.DUPLICATE_EMAIL, exception.getErrorCode());
        verify(userRepository, times(1)).existsByEmail(anyString());

    }

    @Test
    @DisplayName("로그인 성공")
    void loginSuccess() {
        when(encoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.createAccessToken(anyString(), any(UserRole.class))).thenReturn("testToken");
        when(userRepository.findByUsername(validLoginRequest.getUsername()))
                .thenReturn(Optional.of(testUser));

        LoginResponseDTO loginResponse = authService.login(validLoginRequest);

        assertEquals("testToken", loginResponse.getJwtToken());
        assertEquals(testUser.getUsername(), loginResponse.getUser().getUsername());

        verify(userRepository, times(1)).findByUsername(anyString());
        verify(jwtUtil, times(1)).createAccessToken(anyString(), any(UserRole.class));
        verify(encoder, times(1)).matches(anyString(),anyString());

    }

    @Test
    @DisplayName("로그인 실패 - 없는 사용자")
    void loginFailWhenUserNotFound() {
        LoginRequestDTO notExistentUser = LoginRequestDTO.builder()
                .username("notExistentUser")
                .password("password")
                .build();

        when(userRepository.findByUsername(anyString()))
                .thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> authService.login(notExistentUser));

        assertEquals(ErrorCode.FAIL_LOGIN, exception.getErrorCode());

        verify(userRepository, times(1)).findByUsername(anyString());

    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    void loginFailWhenPasswordIsInvalid() {
        LoginRequestDTO IncorrectPasswordUser = LoginRequestDTO.builder()
                .username("testUser")
                .password("incorrectPassword")
                .build();

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(testUser));
        when(encoder.matches(anyString(), anyString())).thenReturn(false);

        CustomException exception = assertThrows(CustomException.class, () -> authService.login(IncorrectPasswordUser));

        assertEquals(ErrorCode.FAIL_LOGIN, exception.getErrorCode());

        verify(encoder, times(1)).matches(anyString(),anyString());

    }

    @Test
    @DisplayName("로그인 실패 - 탈퇴한 회원")
    void loginFailWhenDeletedUser() {
        LoginRequestDTO IncorrectPasswordUser = LoginRequestDTO.builder()
                .username("testUser")
                .password("correctPassword")
                .build();
        User deletedUser = User.builder()
                .username("deletedUser")
                .password("deletedPassword")
                .email("deletedUser@email.com")
                .nickname("deletedUser")
                .isPublic(true)
                .status(UserStatus.LEAVE)
                .userRole(UserRole.CUSTOMER)
                .createdBy("deletedUser")
                .updatedBy("deletedUser")
                .build();

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(deletedUser));
        when(encoder.matches(anyString(), anyString())).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, () -> authService.login(IncorrectPasswordUser));

        assertEquals(ErrorCode.DELETED_USER, exception.getErrorCode());

        verify(userRepository, times(1)).findByUsername(anyString());
        verify(encoder, times(1)).matches(anyString(),anyString());

    }
}