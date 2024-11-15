package org.sparta.foodordermanagementservice.service;

import org.sparta.foodordermanagementservice.dto.UserDTO;
import org.sparta.foodordermanagementservice.entity.UserRole;
import org.sparta.foodordermanagementservice.dto.request.UpdateUserRequestDTO;

public interface UserService {
    UserDTO getUser(String username);
    void updateUser(String username, UpdateUserRequestDTO request);
    void updateUserRole(String username, UserRole role);
    void deleteUser(String username, String deletedBy);
}
