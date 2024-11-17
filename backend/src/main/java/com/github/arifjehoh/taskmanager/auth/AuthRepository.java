package com.github.arifjehoh.taskmanager.auth;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AuthRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
