package com.ilepilov.restfull.controller;

import com.ilepilov.restfull.dto.AddressDto;
import com.ilepilov.restfull.response.AddressRest;
import com.ilepilov.restfull.service.AddressService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
public class AddressController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AddressService addressService;

    @RequestMapping(path = "/{id}/addresses",
            method = RequestMethod.GET,
            produces = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE,
                    "application/hal+json"
            })
    public Resources<AddressRest> getAddresses(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                               @RequestParam(value = "limit", defaultValue = "50") Integer limit,
                                               @PathVariable("id") String userId) {

        List<AddressDto> storedAdderessesDto = addressService.getAddresses(page, limit, userId);
        List<AddressRest> returnedAddresses = storedAdderessesDto.stream()
                .map(ad -> modelMapper.map(ad, AddressRest.class))
                .collect(Collectors.toList());

        returnedAddresses.forEach(ad -> {
            Link self = linkTo(methodOn(AddressController.class).getAddress(userId, ad.getPublicAddressId())).withSelfRel();
            Link userAddresses = linkTo(methodOn(AddressController.class).getAddresses(1, 5, userId)).withRel("userAddresses");

            ad.add(self, userAddresses);
        });

        return new Resources<>(returnedAddresses);
    }

    @RequestMapping(path = "/{userId}/addresses/{addressId}",
            method = RequestMethod.GET,
            produces = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE,
                    "application/hal+json"
            })
    public Resource<AddressRest> getAddress(@PathVariable("userId") String userId,
                                       @PathVariable("addressId") String addressId) {

        AddressDto storedAdderessDto = addressService.getAddress(userId, addressId);
        AddressRest returnedAddress = modelMapper.map(storedAdderessDto, AddressRest.class);

        Link self = linkTo(methodOn(AddressController.class).getAddress(userId, addressId)).withSelfRel();
        Link addresses = linkTo(methodOn(AddressController.class).getAddresses(1, 5, userId)).withRel("addresses");
        Link user = linkTo(methodOn(UserController.class).getUser(userId)).withRel("user");

        returnedAddress.add(self);
        returnedAddress.add(addresses);
        returnedAddress.add(user);

        return new Resource<>(returnedAddress);
    }
}
