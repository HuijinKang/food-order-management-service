package org.sparta.foodordermanagementservice.service;

import org.sparta.foodordermanagementservice.dto.UserDTO;
import org.sparta.foodordermanagementservice.entity.UserRole;

public interface UserService {
    UserDTO getUser(String username);
    void updateUserRole(String username, UserRole role);
}
