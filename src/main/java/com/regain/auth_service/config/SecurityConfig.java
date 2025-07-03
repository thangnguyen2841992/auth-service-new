package com.regain.auth_service.config;

import com.regain.auth_service.model.entity.Role;
import com.regain.auth_service.model.entity.User;
import com.regain.auth_service.service.jwt.JwtFilter;
import com.regain.auth_service.service.role.IRoleService;
import com.regain.auth_service.service.user.IAuthService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    IRoleService   roleService;

    @Autowired
    private IAuthService authService;

    @Bean
    PasswordEncoder passwordEncoder() {  // Mã hóa password
        return new BCryptPasswordEncoder();
    }

    @Bean // Xác thực người dùng
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @PostConstruct
    public void init() throws IOException {
        List<Role> roles = this.roleService.getAllRoles();
        if (roles.isEmpty()) {
            this.roleService.save(new Role("ROLE_ADMIN"));
            this.roleService.save(new Role("ROLE_USER"));
            this.roleService.save(new Role("ROLE_STAFF"));
        }
        Optional<User> userOptional = this.authService.findByUsername("admin");
        if (userOptional.isEmpty()) {
            this.authService.registerAdmin();
        }
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                config -> config
                        .requestMatchers(HttpMethod.GET, EndPoints.PUBLIC_GET).permitAll()
                        .requestMatchers(HttpMethod.POST, EndPoints.PUBLIC_POST).permitAll()
                        .requestMatchers(HttpMethod.POST, EndPoints.ADMIN_POST).hasRole("ADMIN")
        );
        http.cors(cors -> {
            cors.configurationSource(request -> {
                CorsConfiguration corsConfig = new CorsConfiguration();
                corsConfig.addAllowedOrigin(EndPoints.FRONT_END_HOST);
                corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
                corsConfig.addAllowedHeader("*");
                return corsConfig;
            });
        });
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.httpBasic(Customizer.withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

}
