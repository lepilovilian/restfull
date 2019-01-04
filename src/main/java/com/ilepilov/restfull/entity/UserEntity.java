package com.ilepilov.restfull.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "users")
@Data
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 9043470958961650601L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String publicUserId;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, length = 150)
    private String email;
    private String encryptedPassword;
    private String emailVerificationToken;

    // may produce error. initialize with false (dto too)
    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean emailVerificationStatus;
}
