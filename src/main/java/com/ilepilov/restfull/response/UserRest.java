package com.ilepilov.restfull.response;

import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

@Data
public class UserRest extends ResourceSupport {

    private String publicUserId;
    private String firstName;
    private String lastName;
    private String email;
    private List<AddressRest> addresses;
}
