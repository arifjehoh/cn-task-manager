package com.github.arifjehoh.taskmanager.auth;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_MANAGER = "MANAGER";
    private static final String ROLE_MEMBER = "MEMBER";
    private final AuthRepository repository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public void validateUser(String username, String password) {
        if (username == null || password == null) {
            throw new NullPointerException();
        }
        if (username.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("Username or password is empty");
        }
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password is too short");
        }
        if (username.equalsIgnoreCase(password)) {
            throw new IllegalArgumentException("Username and password cannot be the same");
        }
        repository.findByUsername(username)
                  .ifPresent(user -> {
                      if (!passwordEncoder.matches(password, user.getPassword())) {
                          throw new UsernameNotFoundException("User not found");
                      }
                  });
    }

    public UserDTO save(String username, String password) {
        try {
            validateUser(username, password);
        } catch (UsernameNotFoundException e) {
            // User not found, continue
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        User user = new User(username, passwordEncoder.encode(password), ROLE_MEMBER);
        repository.findByUsername(username)
                  .ifPresent(_ -> {
                      throw new IllegalArgumentException("User already exists");
                  });
        return repository.save(user)
                         .toDTO();
    }

    public UserDTO findByUsername(String username) {
        return repository.findByUsername(username)
                         .map(User::toDTO)
                         .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
