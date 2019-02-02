package com.ilepilov.restfull.dto;

import lombok.Data;

@Data
public class AddressDto {

    private Long id;
    private String publicAddressId;
    private String city;
    private String country;
    private String streetName;
    private String postalCode;
    private String type;
}
