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
import org.sparta.foodordermanagementservice.common.exeption.CustomException;
import org.sparta.foodordermanagementservice.dto.UserDTO;
import org.sparta.foodordermanagementservice.dto.request.UpdateUserRequestDTO;
import org.sparta.foodordermanagementservice.entity.User;
import org.sparta.foodordermanagementservice.entity.UserRole;
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
                .username("testuser")
                .password("testpassword")
                .email("test@email.com")
                .nickname("testnickname")
                .isPublic(true)
                .userRole(UserRole.CUSTOMER)
                .createdBy("testuser")
                .updatedBy("testuser")
                .build();
    }
    @Test
    @DisplayName("유저 조회 성공")
    void getUserSuccess() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(testUser));

        UserDTO userDTO = userService.getUser(testUser.getUsername());

        assertEquals(testUser.getUsername(), userDTO.getUsername());
        assertEquals(testUser.getPassword(), userDTO.getPassword());
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

        assertThrows(CustomException.class, () -> userService.getUser("notExistingUsername"));
    }

    @Test
    @DisplayName("유저 정보 수정 성공")
    void updateUserSuccess() {
        UpdateUserRequestDTO request = UpdateUserRequestDTO.builder()
                .nickname("updatedNickname")
                .email("updatedEmail")
                .isPublic(false)
                .password("updatedPassword")
                .build();

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(testUser));

        userService.updateUser(testUser.getUsername(), request);

        verify(userRepository, times(1)).findByUsername(anyString());
        verify(userRepository, times(1)).save(any(User.class));

    }

    @Test
    @DisplayName("유저 정보 수정 실패 - 없는 사용자")
    void updateUserFailWhenUserNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> userService.updateUser("notExistingUsername", any(UpdateUserRequestDTO.class)));

        verify(userRepository, times(1)).findByUsername(anyString());
    }

}