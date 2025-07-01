package com.regain.auth_service.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private Date birthday;

    @Column(nullable = false)
    private Date dateCreated;

    private int gender;

    private Date lastLogin;

    private String avatar;

    private boolean active;

    private String address;

    private String codeActive;

    private boolean isBlock;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_id")
    private Set<Role> roles;
}
