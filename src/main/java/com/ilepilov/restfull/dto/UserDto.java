package com.ilepilov.restfull.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserDto implements Serializable {

    private static final long serialVersionUID = 5746106169640917665L;
    private Long id;
    private String publicUserId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String encryptedPassword;
    private String emailVerificationToken;
    private Boolean emailVerificationStatus = false;
    private List<AddressDto> addresses;
}
