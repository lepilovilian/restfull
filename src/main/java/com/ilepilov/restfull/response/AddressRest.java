package com.ilepilov.restfull.response;

import lombok.Data;

@Data
public class AddressRest {

    private String publicAddressId;
    private String city;
    private String country;
    private String streetName;
    private String postalCode;
    private String type;
}
