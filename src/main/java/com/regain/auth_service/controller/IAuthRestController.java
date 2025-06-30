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
@RequestMapping("/auth-api")
public class IAuthRestController {
    @Autowired
    private IAuthService authService;

    @PostMapping("/register")
    public ResponseEntity<User> createAccount(@RequestBody RegisterForm registerForm) throws IOException {
        User newUser = (User) this.authService.registerUser(registerForm);
//        MessageDTO messageDTO = new MessageDTO();
//        messageDTO.setFrom("nguyenthang29tbdl@gmail.com");
//        messageDTO.setTo(registerForm.getEmail()); //username is Email
//        messageDTO.setActiveCode(account.getCodeActive());
//        messageDTO.setToName(registerForm.getFirstName() + " " +registerForm.getLastName());
//        kafkaTemplate.send("send-email-active", messageDTO);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }
}
