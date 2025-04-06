package com.ticketbooking.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateUserRequest {
    @NotBlank(message = "用戶名不能為空")
    private String username;

    @NotBlank(message = "密碼不能為空")
    private String password;

    @NotBlank(message = "郵箱不能為空")
    @Email(message = "郵箱格式不正確")
    private String email;
} 