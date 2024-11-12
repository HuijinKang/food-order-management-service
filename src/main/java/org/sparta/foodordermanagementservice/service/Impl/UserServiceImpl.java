package org.sparta.foodordermanagementservice.service.Impl;

import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.dto.UserDTO;
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
        return null;
    }
}
