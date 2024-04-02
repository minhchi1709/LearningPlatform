package com.mchis.LearningPlatform.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserChangePasswordDto {
    private String oldPassword;
    private String newPassword;
}
