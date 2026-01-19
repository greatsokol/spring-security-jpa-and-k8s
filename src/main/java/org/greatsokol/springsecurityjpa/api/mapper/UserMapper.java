package org.greatsokol.springsecurityjpa.api.mapper;

import org.greatsokol.springsecurityjpa.api.dto.SignupRequest;
import org.greatsokol.springsecurityjpa.api.dto.UserResponseDto;
import org.greatsokol.springsecurityjpa.repository.entity.UserEntity;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "password", qualifiedByName = "encodedPassword")
    UserEntity toUserEntity(SignupRequest signupRequest, @Context PasswordEncoder passwordEncoder);

    UserResponseDto toUserResponseDto(UserEntity userEntity);

    // Default method to perform the actual password encoding
    @Named("encodedPassword")
    default String encodePassword(String password, @Context PasswordEncoder passwordEncoder) {
        return passwordEncoder.encode(password);
    }
}
