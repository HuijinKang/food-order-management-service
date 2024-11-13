package org.sparta.foodordermanagementservice.service.Impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.sparta.foodordermanagementservice.common.exeption.CustomException;
import org.sparta.foodordermanagementservice.dto.UserDTO;
import org.sparta.foodordermanagementservice.entity.User;
import org.sparta.foodordermanagementservice.entity.UserRole;
import org.sparta.foodordermanagementservice.entity.UserStatus;
import org.sparta.foodordermanagementservice.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Spy
    private ModelMapper modelMapper;

    private User testUser;

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
    }
    @Order(1)
    @Test
    @DisplayName("유저 조회 성공")
    void getUserSuccess() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(testUser));

        UserDTO userDTO = userService.getUser(testUser.getUsername());

        assertEquals(testUser.getUsername(), userDTO.getUsername());
        assertEquals(testUser.getEmail(), userDTO.getEmail());
        assertEquals(testUser.getNickname(), userDTO.getNickname());
        assertEquals(testUser.getIsPublic(), userDTO.getIsPublic());
        assertEquals(testUser.getUserRole(), userDTO.getUserRole());
        assertEquals(testUser.getStatus(), userDTO.getStatus());

        verify(userRepository, times(1)).findByUsername(anyString());
    }

    @Test
    @DisplayName("유저 조회 실패 - 없는 사용자")
    void getUserFailWhenUserNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> userService.getUser("notExistingUser"));
    }

    @Test
    @DisplayName("유저 탈퇴 성공")
    void deleteUserSuccess() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(testUser));

        userService.deleteUser(testUser.getUsername());

        verify(userRepository, times(1)).findByUsername(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("유저 탈퇴 실패 - 없는 사용자")
    void deleteUserFailWhenUserNotFound() {
       when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> userService.deleteUser("notExistingUser"));

        verify(userRepository, times(1)).findByUsername(anyString());
    }

    @Test
    @DisplayName("유저 탈퇴 실패 - 마스터 사용자 탈퇴")
    void deleteUserFailWhenUserIsMaster() {
        User masterUser = User.builder()
                .username("masterUser")
                .password("masterPassword")
                .email("master@email.com")
                .nickname("masterNickname")
                .isPublic(false)
                .userRole(UserRole.MASTER)
                .status(UserStatus.ACTIVE)
                .createdBy("masterUser")
                .updatedBy("masterUser")
                .build();

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(masterUser));

        assertThrows(CustomException.class, () -> userService.deleteUser(masterUser.getUsername()));

        verify(userRepository, times(1)).findByUsername(anyString());
    }

}