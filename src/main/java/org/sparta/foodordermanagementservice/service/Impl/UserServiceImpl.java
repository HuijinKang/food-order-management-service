package org.sparta.foodordermanagementservice.service.Impl;

import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.common.exeption.CustomException;
import org.sparta.foodordermanagementservice.common.exeption.ErrorCode;
import org.sparta.foodordermanagementservice.dto.UserDTO;
import org.sparta.foodordermanagementservice.entity.User;
import org.sparta.foodordermanagementservice.repository.UserRepository;
import org.sparta.foodordermanagementservice.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDTO getUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return UserDTO.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .status(user.getStatus())
                .isPublic(user.getIsPublic())
                .userRole(user.getUserRole())
                .nickname(user.getNickname())
                .build();
    }
}
