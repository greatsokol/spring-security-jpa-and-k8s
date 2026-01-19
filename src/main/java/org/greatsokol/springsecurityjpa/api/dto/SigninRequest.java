package org.greatsokol.springsecurityjpa.api.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SigninRequest {
    private final String login;
    private final String password;
}
