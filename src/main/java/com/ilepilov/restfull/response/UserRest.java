package com.ilepilov.restfull.response;

import lombok.Data;

import java.util.List;

@Data
public class UserRest {

    private String publicUserId;
    private String firstName;
    private String lastName;
    private String email;
    private List<AddressRest> addresses;
}
