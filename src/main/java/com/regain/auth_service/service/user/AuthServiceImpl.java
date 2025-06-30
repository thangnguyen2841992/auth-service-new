package com.regain.auth_service.service.user;

import com.regain.auth_service.model.dto.JwtResponse;
import com.regain.auth_service.model.dto.LoginForm;
import com.regain.auth_service.model.dto.RegisterForm;
import com.regain.auth_service.model.entity.Role;
import com.regain.auth_service.model.entity.User;
import com.regain.auth_service.repository.IRoleRepository;
import com.regain.auth_service.repository.IUserRepository;
import com.regain.auth_service.service.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    private static final String AVATAR_DEFAULT = "https://firebasestorage.googleapis.com/v0/b/thangdeptrai-9efec.appspot.com/o/images%2Favatar2.jpg?alt=media&token=3ca1a511-1e03-448a-9de2-6ea880e5071c";

    @Override
    public Optional<User> findByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    @Override
    public Object registerUser(RegisterForm registerForm) throws IOException {
        String messageFailed = "";

        boolean isExistUsername = this.userRepository.existsByUsername(registerForm.getUsername());
        boolean isExistEmail = this.userRepository.existsByEmail(registerForm.getEmail());
        boolean isExistPhoneNumber = this.userRepository.existsByPhoneNumber(registerForm.getPhoneNumber());
        boolean isMatchedPassword = registerForm.getConfirmPassword().equals(registerForm.getPassword());
        if (!isExistUsername && isMatchedPassword && !isExistEmail && !isExistPhoneNumber) {
            User newUser = new User();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date birthDate = formatter.parse(registerForm.getBirthDate());
                newUser.setBirthday(birthDate);
            } catch (ParseException e) {
                throw new RuntimeException("Invalid birthday");
            }
            newUser.setFirstName(registerForm.getFirstName());
            newUser.setLastName(registerForm.getLastName());
            newUser.setUsername(registerForm.getUsername());
            newUser.setEmail(registerForm.getEmail());
            newUser.setPassword(passwordEncoder.encode(registerForm.getPassword()));
            newUser.setPhoneNumber(registerForm.getPhoneNumber());
            newUser.setAddress(registerForm.getAddress());
            newUser.setCodeActive(createActiveCode());
            newUser.setActive(false);
            newUser.setGender(registerForm.getGender());
            newUser.setAvatar(AVATAR_DEFAULT);
            return this.userRepository.save(newUser);
        } else {
            if (isExistUsername) {
                messageFailed += " Username is existed!";
            }
            if (isExistEmail) {
                messageFailed += " Email is existed!";
            }
            if (isExistPhoneNumber) {
                messageFailed +=  " Phone number is existed!";
            }
            if (!isMatchedPassword) {
                messageFailed += " Password is not matched!";
            }
            return messageFailed;
        }

    }

    @Override
    public ResponseEntity<?> login(LoginForm loginForm) {
        // Xác thực người dùng bằng tên đăng nhập và mật khẩu
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword())
            );
            // Nếu xác thực thành công, tạo token JWT
            if (authentication.isAuthenticated()) {
                final String jwt = jwtService.generateToken(loginForm.getUsername());
                return ResponseEntity.ok(new JwtResponse(jwt));
            }
        } catch (AuthenticationException e) {
            // Xác thực không thành công, trả về lỗi hoặc thông báo
            return ResponseEntity.badRequest().body("Username or password is incorrect");
        }
        return ResponseEntity.badRequest().body("Authentication failed");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), rolesToAuthorities(user.getRoles()));
    }
    private String createActiveCode() {
        return UUID.randomUUID().toString();
    }

    private Collection<? extends GrantedAuthority> rolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toList());
    }
    private Set<Role> getRoleUser() {
        Set<Role> roles = new HashSet<>();
        Optional<Role> roleOptional = this.roleRepository.findByRoleName("ROLE_USER");
        roleOptional.ifPresent(roles::add);
        return roles;
    }
}
