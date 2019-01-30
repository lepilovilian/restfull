package com.ilepilov.restfull.service.impl;

import com.ilepilov.restfull.dto.AddressDto;
import com.ilepilov.restfull.entity.AddressEntity;
import com.ilepilov.restfull.repository.AddressRepo;
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

    @Override
    public List<AddressDto> getAddresses(Integer page, Integer limit, String userId) {

        if (isNull(userId)) {
            throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }

        Pageable pageable = PageRequest.of(--page, limit);
        List<AddressEntity> storedAddressesEntity = addressRepo.findAllByUserId(pageable, userId);

        List<AddressDto> storedAddressesDto = storedAddressesEntity.stream()
                .map(ad -> modelMapper.map(ad, AddressDto.class)).collect(Collectors.toList());

        return storedAddressesDto;
    }
}
