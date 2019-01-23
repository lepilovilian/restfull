package com.ilepilov.restfull.requestmodel;

import lombok.Data;

@Data
public class UserDetailsRequestModel {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
