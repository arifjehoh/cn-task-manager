package com.github.arifjehoh.taskmanager.configurations.web;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {
    private final RsaKeyProperties rsaKeys;
    private final int passwordStrength;

    public WebSecurityConfiguration(RsaKeyProperties rsaKeys,
                                    @Value("${spring.security.password-strength}") int passwordStrength) {
        this.rsaKeys = rsaKeys;
        this.passwordStrength = passwordStrength;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                   .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                   .authorizeHttpRequests((authorize) -> authorize.requestMatchers("/api/auth/**")
                                                                  .permitAll())
                   .authorizeHttpRequests((authorize) -> authorize.requestMatchers("/api/v1/**")
                                                                  .authenticated())
                   .authorizeHttpRequests((authorize) -> authorize.anyRequest()
                                                                  .denyAll())
                   .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()))
                   .build();
    }

    @Bean
    public FilterRegistrationBean<JwtLoginFilter> jwtLoginFilter() {
        FilterRegistrationBean<JwtLoginFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtLoginFilter(jwtDecoder()));
        registrationBean.addUrlPatterns("/api/v1/**");
        return registrationBean;
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(rsaKeys.publicKey()).privateKey(rsaKeys.privateKey())
                                                         .build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey())
                               .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(passwordStrength);
    }

}
