package org.greatsokol.springsecurityjpa.api.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SigninResponse {
    private final String accessToken;
}
