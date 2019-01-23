package com.ilepilov.restfull.response;

import lombok.Data;

@Data
public class UserRest {

    private String publicUserId;
    private String firstName;
    private String lastName;
    private String email;
}
