package com.regain.auth_service.service.jwt;

import com.regain.auth_service.model.entity.Role;
import com.regain.auth_service.model.entity.User;
import com.regain.auth_service.service.user.IAuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {
    private final String JWT_SECRET = "839874983AVCfsds7274892378947238947238avcvsdsdw9472398723984723987239";
    private final String ROLE_ADMIN = "ROLE_ADMIN";
    private final String ROLE_USER = "ROLE_USER";
    private final String ROLE_STAFF = "ROLE_STAFF";
    private final long EXPIRATION_TIME = 5 * 60 * 1000;

    @Autowired
    private IAuthService userService;


    //Tạo token dựa trên username
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();

        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            boolean isAdmin = false;
            boolean isUser = false;
            boolean isStaff = false;

            String fullName = user.getFirstName() + " " + user.getLastName();

            if (!user.getRoles().isEmpty()) {
                Set<Role> roles = user.getRoles();
                for (Role role : roles) {
                    if (ROLE_ADMIN.equals(role.getRoleName())) {
                        isAdmin = true;
                    } else if (ROLE_USER.equals(role.getRoleName())) {
                        isUser = true;
                    } else {
                        isStaff = true;
                    }
                }
            }
            claims.put("username", user.getUsername());
            claims.put("fullName", fullName);
            claims.put("isAdmin", isAdmin);
            claims.put("isUser", isUser);
            claims.put("isStaff", isStaff);
            claims.put("avatar", user.getAvatar());
            claims.put("email", user.getEmail());
            claims.put("phoneNumber", user.getPhoneNumber());
            return generateToken(user.getUsername(), claims);

        } else {
            return "User not found";
        }

    }

    private String generateToken(String username, Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, getSigneKey())
                .compact();
    }


    // Lấy serect key
    private byte[] getSigneKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET);
        return Keys.hmacShaKeyFor(keyBytes).getEncoded();
    }

    // Trích xuất thông tin
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSigneKey()).parseClaimsJws(token).getBody();
    }

    // Trích xuất thông tin cho 1 claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    // Kiểm tra tời gian hết hạn từ JWT
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Kiểm tra tời gian hết hạn từ JWT
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Kiểm tra cái JWT đã hết hạn
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Kiểm tra tính hợp lệ
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        System.out.println(username);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


}
