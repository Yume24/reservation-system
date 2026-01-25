package com.ajaros.reservationsystem.users.dtos;

import com.ajaros.reservationsystem.validation.ValidPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UpdatePasswordRequest(
    @Schema(example = "OldPassword123!")
        @NotBlank(message = "Password cannot be blank")
        @ValidPassword
        String oldPassword,
    @Schema(example = "NewPassword123!")
        @NotBlank(message = "Password cannot be blank")
        @ValidPassword
        String newPassword) {}
