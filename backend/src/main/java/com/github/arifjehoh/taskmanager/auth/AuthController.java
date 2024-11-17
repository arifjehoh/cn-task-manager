package com.github.arifjehoh.taskmanager.auth;

import com.github.arifjehoh.taskmanager.utils.JwtUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final JwtUtils jwtUtils;

    public AuthController(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @RequestMapping("/login")
    public String login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        if (username == null || password == null) {
            throw new ResponseStatusException(BAD_REQUEST, "Failed to login");
        }
        if (username.isEmpty() || password.isEmpty()) {
            throw new ResponseStatusException(BAD_REQUEST, "Failed to login");
        }
        return jwtUtils.generateToken(username);
    }
}
