package com.restapi.project_AI_diary_backend.domain.user.repository;

import com.restapi.project_AI_diary_backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    //Optional<User> findByUsername(String username);
}
