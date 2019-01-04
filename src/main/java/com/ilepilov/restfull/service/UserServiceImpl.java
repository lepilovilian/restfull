package com.ilepilov.restfull.service;

import com.ilepilov.restfull.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {


    @Override
    public UserDto save(UserDto userDto) {
        UserDto returnedDto = new UserDto();

        return null;
    }
}
