package org.greatsokol.springsecurityjpa.service;

import lombok.RequiredArgsConstructor;
import org.greatsokol.springsecurityjpa.repository.UserRepository;
import org.greatsokol.springsecurityjpa.repository.entity.UserEntity;
import org.greatsokol.springsecurityjpa.security.UserDetailsImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    public UserEntity addUser(UserEntity newUser) {
        return userRepository.save(newUser);
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository
                .findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException(login));
        return UserDetailsImpl.build(userEntity);
    }

    public boolean existsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
