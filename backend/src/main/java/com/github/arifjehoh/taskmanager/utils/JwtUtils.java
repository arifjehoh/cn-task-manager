package com.github.arifjehoh.taskmanager.utils;

import com.github.arifjehoh.taskmanager.auth.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Configuration
public class JwtUtils {
    private final String issuerUri;
    private final JwtEncoder encoder;

    public JwtUtils(@Value(value = "${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuerUri, JwtEncoder encoder) {
        this.issuerUri = issuerUri;
        this.encoder = encoder;
    }

    public String generateToken(UserDTO user) {
        Instant iat = Instant.now();
        Instant exp = iat.plus(1, ChronoUnit.HOURS);
        JwtClaimsSet claims = JwtClaimsSet.builder()
                                          .issuer(issuerUri)
                                          .subject(user.username())
                                          .claim("authorities", user.authority())
                                          .issuedAt(iat)
                                          .expiresAt(exp)
                                          .build();

        return this.encoder.encode(JwtEncoderParameters.from(claims))
                           .getTokenValue();
    }
}
