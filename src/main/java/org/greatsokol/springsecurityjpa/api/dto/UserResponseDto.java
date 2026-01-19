package org.greatsokol.springsecurityjpa.api.dto;


import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class UserResponseDto {
    private final UUID id;
    private final String name;
    private final String email;
    private final String login;
}
