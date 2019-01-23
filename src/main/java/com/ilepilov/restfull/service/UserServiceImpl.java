package com.ilepilov.restfull.service;

import com.ilepilov.restfull.dto.UserDto;
import com.ilepilov.restfull.entity.UserEntity;
import com.ilepilov.restfull.exception.UserServiceException;
import com.ilepilov.restfull.repository.UserRepo;
import com.ilepilov.restfull.response.ErrorMessages;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public UserDto save(UserDto userDto) {

        UserEntity existedUser = userRepo.findByEmail(userDto.getEmail());
        if (nonNull(existedUser)) {
            throw new RuntimeException("User already exists");
        }

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userDto, userEntity);

        userEntity.setPublicUserId(utils.generateUserId(30));
        userEntity.setEncryptedPassword(passwordEncoder.encode(userDto.getPassword()));

        UserEntity storedUserEntity = userRepo.save(userEntity);

        UserDto returnedDto = new UserDto();
        BeanUtils.copyProperties(storedUserEntity, returnedDto);

        return returnedDto;
    }

    @Override
    public UserDto getUser(String email) {
        UserEntity storedUser = userRepo.findByEmail(email);

        UserDto returnedUser = new UserDto();
        BeanUtils.copyProperties(storedUser, returnedUser);

        return returnedUser;
    }

    @Override
    public UserDto getUserById(String id) {
        if (nonNull(id)) {
            UserDto returnedUser = new UserDto();

            UserEntity storedUserEntity = userRepo.findByPublicUserId(id);
            BeanUtils.copyProperties(storedUserEntity, returnedUser);

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

        BeanUtils.copyProperties(userDto, storedUserEntity);
        UserEntity updatedUserEntity = userRepo.save(storedUserEntity);

        UserDto updatedUserDto = new UserDto();
        BeanUtils.copyProperties(updatedUserEntity, updatedUserDto);

        return updatedUserDto;
    }

    @Override
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
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(userEntity, userDto);
            userDtos.add(userDto);
        }

        return userDtos;
    }
}
