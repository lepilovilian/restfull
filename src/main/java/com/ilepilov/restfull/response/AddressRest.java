package com.ilepilov.restfull.response;

import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

@Data
public class AddressRest extends ResourceSupport {

    private String publicAddressId;
    private String city;
    private String country;
    private String streetName;
    private String postalCode;
    private String type;
}
