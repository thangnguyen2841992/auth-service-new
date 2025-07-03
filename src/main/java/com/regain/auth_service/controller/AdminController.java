package com.regain.auth_service.controller;

import com.regain.auth_service.model.dto.RegisterForm;
import com.regain.auth_service.model.entity.User;
import com.regain.auth_service.service.user.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/admin-api")
public class AdminController {
    @Autowired
    private IAuthService authService;

    @PostMapping("/registerStaff")
    public ResponseEntity<Object> registerStaff(@RequestBody RegisterForm registerForm) throws IOException {
        User newUser = (User) this.authService.registerStaff(registerForm);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }
}
