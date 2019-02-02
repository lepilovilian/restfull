package com.ilepilov.restfull.service;

import com.ilepilov.restfull.dto.AddressDto;

import java.util.List;

public interface AddressService {

    List<AddressDto> getAddresses(Integer page, Integer limit, String userId);

    AddressDto getAddress(String userId, String addressId);
}
