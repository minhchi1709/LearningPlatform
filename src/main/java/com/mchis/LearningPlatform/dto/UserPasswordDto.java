package com.mchis.LearningPlatform.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class UserPasswordDto {
    private String email;
    private String password;
}
