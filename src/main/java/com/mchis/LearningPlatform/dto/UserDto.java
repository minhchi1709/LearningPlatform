package com.mchis.LearningPlatform.dto;

import lombok.Builder;

@Builder
public record UserDto(
        String username,
        String firstname,
        String lastname,
        String email
) {

}
