package org.sparta.foodordermanagementservice.service;

import org.sparta.foodordermanagementservice.dto.UserDTO;

public interface UserService {
    UserDTO getUser(String username);
}
