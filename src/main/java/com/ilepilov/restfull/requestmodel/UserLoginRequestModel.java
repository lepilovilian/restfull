package com.ilepilov.restfull.requestmodel;

import lombok.Data;

@Data
public class UserLoginRequestModel {

    private String email;
    private String password;
}
