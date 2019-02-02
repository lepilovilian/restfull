package com.ilepilov.restfull.service.impl;

import com.ilepilov.restfull.dto.AddressDto;
import com.ilepilov.restfull.entity.AddressEntity;
import com.ilepilov.restfull.entity.UserEntity;
import com.ilepilov.restfull.repository.AddressRepo;
import com.ilepilov.restfull.repository.UserRepo;
import com.ilepilov.restfull.response.ErrorMessages;
import com.ilepilov.restfull.service.AddressService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private UserRepo userRepo;

    @Override
    public List<AddressDto> getAddresses(Integer page, Integer limit, String userId) {

        Pageable pageable = PageRequest.of(--page, limit);

        UserEntity userEntity = userRepo.findByPublicUserId(userId);
        if (isNull(userEntity)) {
            throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }

        List<AddressEntity> storedAddressesEntity = addressRepo.findAllByUserId(pageable, userEntity.getId());
        List<AddressDto> storedAddressesDto = storedAddressesEntity.stream()
                .map(ad -> modelMapper.map(ad, AddressDto.class)).collect(Collectors.toList());

        return storedAddressesDto;
    }

    @Override
    public AddressDto getAddress(String userId, String addressId) {
        UserEntity userEntity = userRepo.findByPublicUserId(userId);

        if (isNull(userEntity)) {
            throw new UsernameNotFoundException("User was not found");
        }

        AddressEntity addressEntity = addressRepo.findByPublicAddressIdAndUserId(addressId, userEntity.getId());
        return modelMapper.map(addressEntity, AddressDto.class);
    }
}
