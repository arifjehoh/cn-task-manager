package com.github.arifjehoh.taskmanager.auth;

import com.github.arifjehoh.taskmanager.utils.JwtUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final JwtUtils jwtUtils;
    private final AuthService service;

    public AuthController(JwtUtils jwtUtils,
                          AuthService service) {
        this.jwtUtils = jwtUtils;
        this.service = service;
    }

    public record LoginRequest(String username, String password) {
    }

    @PostMapping("/signin")
    public String login(@RequestBody LoginRequest body) {
        String username = body.username();
        String password = body.password();
        try {
            service.validateUser(username, password);
            UserDTO user = service.findByUsername(username);
            return jwtUtils.generateToken(user);
        } catch (Exception e) {
            throw new ResponseStatusException(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/signup")
    public String signup(@RequestBody LoginRequest body) {
        String username = body.username();
        String password = body.password();
        try {
            UserDTO user = service.save(username, password);
            return jwtUtils.generateToken(user);
        } catch (Exception e) {
            throw new ResponseStatusException(BAD_REQUEST, "Failed to login");
        }
    }
}
