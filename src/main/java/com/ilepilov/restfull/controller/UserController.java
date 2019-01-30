package com.ilepilov.restfull.controller;

import com.ilepilov.restfull.dto.UserDto;
import com.ilepilov.restfull.exception.UserServiceException;
import com.ilepilov.restfull.requestmodel.UserDetailsRequestModel;
import com.ilepilov.restfull.response.*;
import com.ilepilov.restfull.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping(path = "/{id}",
            method = RequestMethod.GET,
            produces = {
                MediaType.APPLICATION_XML_VALUE,
                MediaType.APPLICATION_JSON_VALUE
            }
    )
    public UserRest getUser(@PathVariable("id") String id) {
        UserDto userDto = userService.getUserById(id);
        UserRest returnedUser = modelMapper.map(userDto, UserRest.class);

        return returnedUser;
    }

    @RequestMapping(
            method = RequestMethod.POST,
            produces = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            },

            consumes = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
        if (userDetails.getEmail().isEmpty() || userDetails.getPassword().isEmpty()) {
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }

        if (userDetails.getFirstName().isEmpty()) {
            throw new NullPointerException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }

        UserDto userDto = modelMapper.map(userDetails, UserDto.class);
        userDto = userService.save(userDto);
        UserRest userRest = modelMapper.map(userDto, UserRest.class);

        return userRest;
    }

    @RequestMapping(
            method = RequestMethod.PUT,
            produces = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            },

            consumes = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            },
            path = "/{id}"
    )
    public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {
        if (userDetails.getEmail().isEmpty() || userDetails.getPassword().isEmpty()) {
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }

        if (userDetails.getFirstName().isEmpty()) {
            throw new NullPointerException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }

        UserDto updatedUser = modelMapper.map(userDetails, UserDto.class);

        updatedUser = userService.updateUser(id, updatedUser);
        UserRest userRest = modelMapper.map(updatedUser, UserRest.class);

        return userRest;
    }

    @RequestMapping(
            method = RequestMethod.DELETE,
            produces = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            },

            consumes = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            },
            path = "/{id}"
    )
    public OperationStatusModel deleteUser(@PathVariable String id) {
        userService.deleteUser(id);

        OperationStatusModel operationStatusModel = new OperationStatusModel();
        operationStatusModel.setOperation(RequestOperationName.DELETE.name());
        operationStatusModel.setStatus(RequestOperationStatus.SUCCES.name());

        return operationStatusModel;
    }

    @RequestMapping(
            method = RequestMethod.GET,
            produces = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                   @RequestParam(value = "limit", defaultValue = "2") Integer limit) {
        List<UserRest> usersRest = new ArrayList<>();

        List<UserDto> userDtos = userService.getUsers(page, limit);
        for (UserDto userDto : userDtos) {
            UserRest userRest = modelMapper.map(userDto, UserRest.class);
            usersRest.add(userRest);
        }

        return usersRest;
    }
}