package org.greatsokol.springsecurityjpa.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.greatsokol.springsecurityjpa.api.dto.SigninRequest;
import org.greatsokol.springsecurityjpa.api.dto.SigninResponse;
import org.greatsokol.springsecurityjpa.api.dto.SignupRequest;
import org.greatsokol.springsecurityjpa.api.dto.UserResponseDto;
import org.greatsokol.springsecurityjpa.api.mapper.UserMapper;
import org.greatsokol.springsecurityjpa.repository.entity.UserEntity;
import org.greatsokol.springsecurityjpa.security.JwtCore;
import org.greatsokol.springsecurityjpa.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Log4j2
public class SecurityController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtCore jwtCore;

    @PostMapping("signup")
    ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        if (userService.existsByLogin(signupRequest.getLogin())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Already exists. Use another login");
        }

        if (userService.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Already exists. Use another email");
        }

        UserEntity userEntity = userMapper.toUserEntity(signupRequest, passwordEncoder);

        userEntity.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        UserResponseDto addedUser = userMapper.toUserResponseDto(userService.addUser(userEntity));//, passwordEncoder));

        return ResponseEntity.ok(addedUser);
    }

    @PostMapping("signin")
    ResponseEntity<?> signin(@RequestBody SigninRequest signinRequest) {
        try {
            UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(
                    signinRequest.getLogin(),
                    signinRequest.getPassword()
            );
            Authentication authentication = authenticationManager.authenticate(upat);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtCore.generateToken(authentication);
            return ResponseEntity.ok(new SigninResponse(jwt));
        } catch (BadCredentialsException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
