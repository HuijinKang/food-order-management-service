package org.sparta.foodordermanagementservice.service;

import org.sparta.foodordermanagementservice.dto.UserDTO;
import org.sparta.foodordermanagementservice.dto.request.UpdateUserRequestDTO;

public interface UserService {
    UserDTO getUser(String username);
    void updateUser(String username, UpdateUserRequestDTO request);
}
