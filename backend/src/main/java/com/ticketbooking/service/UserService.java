package com.ticketbooking.service;

import com.ticketbooking.dto.UserDTO;
import com.ticketbooking.dto.CreateUserRequest;
import java.util.List;

public interface UserService {
    UserDTO registerUser(CreateUserRequest request);
    UserDTO registerMerchant(CreateUserRequest request);
    UserDTO getUserById(Long id);
    UserDTO getUserByUsername(String username);
    List<UserDTO> getAllUsers();
    UserDTO updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);
} 