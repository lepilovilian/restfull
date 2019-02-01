package com.ilepilov.restfull.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class UserRest {

    private String publicUserId;
    private String firstName;
    private String lastName;
    private String email;
    @JsonIgnore
    private List<AddressRest> addresses;
}
