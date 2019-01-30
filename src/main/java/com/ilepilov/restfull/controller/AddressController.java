package com.ilepilov.restfull.controller;

import com.ilepilov.restfull.dto.AddressDto;
import com.ilepilov.restfull.response.AddressRest;
import com.ilepilov.restfull.service.AddressService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController("/addresses")
public class AddressController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AddressService addressService;

    @RequestMapping(path = "/users{id}/addresses}",
            method = RequestMethod.GET,
            produces = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            })
    public ResponseEntity<Object> getAddresses(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                               @RequestParam(value = "limit", defaultValue = "50") Integer limit,
                                               @PathVariable("id") String userId) {

        List<AddressDto> storedAdderessesDto = addressService.getAddresses(page, limit, userId);
        List<AddressRest> returnedAddresses = storedAdderessesDto.stream()
                .map(ad -> modelMapper.map(ad, AddressRest.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(returnedAddresses, HttpStatus.OK);
    }
}
