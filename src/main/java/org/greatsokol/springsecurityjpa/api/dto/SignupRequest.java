package org.greatsokol.springsecurityjpa.api.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SignupRequest {
    private final String name;
    private final String email;
    private final String login;
    private final String password;
}
