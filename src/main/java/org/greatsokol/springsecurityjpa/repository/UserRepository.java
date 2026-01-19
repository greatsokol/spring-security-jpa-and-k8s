package org.greatsokol.springsecurityjpa.repository;

import org.greatsokol.springsecurityjpa.repository.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByName(String username);

    Optional<UserEntity> findByLogin(String login);

    boolean existsByLogin(String login);

    boolean existsByEmail(String email);
}
