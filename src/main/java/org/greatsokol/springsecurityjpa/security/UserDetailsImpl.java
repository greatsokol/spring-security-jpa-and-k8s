package org.greatsokol.springsecurityjpa.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.greatsokol.springsecurityjpa.repository.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private UUID id;
    private String name;
    private String email;
    private String login;
    private String password;

    public static UserDetails build(UserEntity user) {
//        return User.builder()
//                .username(user.getName())
//                .password(user.getPassword())
//                .roles("USER", "ADMIN")
//                .build();

        return new UserDetailsImpl(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getPassword()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority("ADMIN"),
                new SimpleGrantedAuthority("ROLE_ADMIN")
        );
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }
}
