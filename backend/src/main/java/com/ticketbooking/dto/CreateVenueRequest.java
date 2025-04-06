package com.ticketbooking.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreateVenueRequest {
    @NotBlank(message = "場地名稱不能為空")
    private String name;

    @NotBlank(message = "場地位置不能為空")
    private String location;

    @NotBlank(message = "場地描述不能為空")
    private String description;

    @NotNull(message = "容量不能為空")
    @Min(value = 1, message = "容量必須大於0")
    private Integer capacity;

    @NotNull(message = "每小時價格不能為空")
    @Min(value = 0, message = "價格不能為負數")
    private BigDecimal pricePerHour;
} 