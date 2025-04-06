package com.ticketbooking.controller;

import com.ticketbooking.dto.UserDTO;
import com.ticketbooking.dto.CreateUserRequest;
import com.ticketbooking.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Tag(name = "使用者管理", description = "使用者相關的 API")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @Operation(summary = "註冊一般使用者")
    public ResponseEntity<Map<String, Object>> registerUser(@Valid @RequestBody CreateUserRequest request) {
        try {
            UserDTO user = userService.registerUser(request);
            return success(user);
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    @PostMapping("/register/merchant")
    @Operation(summary = "註冊商家")
    public ResponseEntity<Map<String, Object>> registerMerchant(@Valid @RequestBody CreateUserRequest request) {
        try {
            UserDTO merchant = userService.registerMerchant(request);
            return success(merchant);
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "取得使用者資訊")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
        try {
            UserDTO user = userService.getUserById(id);
            return success(user);
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    @GetMapping
    @Operation(summary = "取得所有使用者")
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        try {
            List<UserDTO> users = userService.getAllUsers();
            return success(users);
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新使用者資訊")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO user) {
        try {
            UserDTO updatedUser = userService.updateUser(id, user);
            return success(updatedUser);
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "刪除使用者")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return success("使用者已成功刪除");
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }
} 