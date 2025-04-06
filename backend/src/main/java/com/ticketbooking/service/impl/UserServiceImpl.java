package com.ticketbooking.service.impl;

import com.ticketbooking.dto.UserDTO;
import com.ticketbooking.dto.CreateUserRequest;
import com.ticketbooking.model.User;
import com.ticketbooking.model.UserRole;
import com.ticketbooking.repository.UserRepository;
import com.ticketbooking.service.UserService;
import com.ticketbooking.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDTO registerUser(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("使用者名稱已存在");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("電子郵件已存在");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole(UserRole.USER);

        return userMapper.toDTO(userRepository.save(user));
    }

    @Override
    public UserDTO registerMerchant(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("使用者名稱已存在");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("電子郵件已存在");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole(UserRole.MERCHANT);

        return userMapper.toDTO(userRepository.save(user));
    }

    @Override
    public UserDTO getUserById(Long id) {
        return userMapper.toDTO(userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到使用者")));
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        return userMapper.toDTO(userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("找不到使用者")));
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到使用者"));

        user.setEmail(userDTO.getEmail());

        return userMapper.toDTO(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("找不到使用者");
        }
        userRepository.deleteById(id);
    }
} 