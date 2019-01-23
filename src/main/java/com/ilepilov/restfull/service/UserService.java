package com.ilepilov.restfull.service;

import com.ilepilov.restfull.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserDto save(UserDto userDto);

    UserDto getUser(String email);

    UserDto getUserById(String id);

    UserDto updateUser(String id, UserDto userToUpdate);

    void deleteUser(String id);

    List<UserDto> getUsers(Integer page, Integer limit);
}
