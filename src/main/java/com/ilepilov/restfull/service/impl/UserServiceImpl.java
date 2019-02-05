package com.ilepilov.restfull.service.impl;

import com.ilepilov.restfull.dto.UserDto;
import com.ilepilov.restfull.entity.UserEntity;
import com.ilepilov.restfull.exception.UserServiceException;
import com.ilepilov.restfull.repository.UserRepo;
import com.ilepilov.restfull.response.ErrorMessages;
import com.ilepilov.restfull.service.UserService;
import com.ilepilov.restfull.service.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private Utils utils;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDto save(UserDto userDto) {

        UserEntity existedUser = userRepo.findByEmail(userDto.getEmail());
        if (nonNull(existedUser)) {
            throw new RuntimeException("User already exists");
        }

        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        userEntity.setPublicUserId(utils.generatePublicId(30));
        userEntity.getAddresses().forEach(ad -> {
            ad.setPublicAddressId(utils.generatePublicId(30));
            ad.setUser(userEntity);
        });
        userEntity.setEncryptedPassword(passwordEncoder.encode(userDto.getPassword()));

        UserEntity storedUserEntity = userRepo.save(userEntity);
        UserDto returnedDto = modelMapper.map(storedUserEntity, UserDto.class);

        return returnedDto;
    }

    @Override
    public UserDto getUser(String email) {
        UserEntity storedUser = userRepo.findByEmail(email);

        UserDto returnedUser = modelMapper.map(storedUser, UserDto.class);

        return returnedUser;
    }

    @Override
    public UserDto getUserById(String id) {
        if (nonNull(id)) {
            UserEntity storedUserEntity = userRepo.findByPublicUserId(id);
            UserDto returnedUser = modelMapper.map(storedUserEntity, UserDto.class);

            return returnedUser;
        } else {
            throw new RuntimeException("User doesn't exist");
        }
    }

    @Override
    public UserDto updateUser(String id, UserDto userDto) {
        UserEntity storedUserEntity = userRepo.findByPublicUserId(id);

        if (isNull(storedUserEntity)) {
            throw new UserServiceException(ErrorMessages.COULD_NOT_UPDATE_RECORD.getErrorMessage());
        }

        String userDtoPassword = userDto.getPassword();
        userDto.setId(storedUserEntity.getId());
        userDto.setPublicUserId(id);
        userDto.setEncryptedPassword(passwordEncoder.encode(userDtoPassword));

        storedUserEntity = modelMapper.map(userDto, UserEntity.class);
        UserEntity updatedUserEntity = userRepo.save(storedUserEntity);

        UserDto updatedUserDto = modelMapper.map(updatedUserEntity, UserDto.class);

        return updatedUserDto;
    }

    @Override
//    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity storedUser = userRepo.findByEmail(email);

        if (isNull(storedUser)) {
            throw new UsernameNotFoundException("User doesn't exist");
        }

        return new User(storedUser.getEmail(), storedUser.getEncryptedPassword(), new ArrayList<>());
    }

    @Override
    public void deleteUser(String id) {
        UserEntity storedUser = userRepo.findByPublicUserId(id);

        if (isNull(storedUser)) {
            throw new UserServiceException(ErrorMessages.COULD_NOT_DELETE_RECORD.getErrorMessage());
        }

        userRepo.delete(storedUser);
    }

    @Override
    public List<UserDto> getUsers(Integer page, Integer limit) {
        List<UserDto> userDtos = new ArrayList<>();

        Pageable pageableRequest = PageRequest.of(--page, limit);
        Page<UserEntity> usersPage = userRepo.findAll(pageableRequest);

        List<UserEntity> userEntities = usersPage.getContent();
        for (UserEntity userEntity : userEntities) {
            UserDto userDto = modelMapper.map(userEntity, UserDto.class);
            userDtos.add(userDto);
        }

        return userDtos;
    }

    @Override
    public Boolean verifyEmailToken(String token) {
        return true;
    }
}
