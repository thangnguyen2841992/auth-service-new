package com.regain.auth_service.service.user;

import com.regain.auth_service.model.dto.LoginForm;
import com.regain.auth_service.model.dto.RegisterForm;
import com.regain.auth_service.model.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.Optional;

public interface IAuthService extends UserDetailsService {
    Optional<User> findByUsername(String username);

    Object registerUser(RegisterForm registerForm) throws IOException;
    void registerAdmin() throws IOException;

    ResponseEntity<?> login(LoginForm loginForm);
}
