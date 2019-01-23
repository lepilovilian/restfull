package com.ilepilov.restfull.controller;

import com.ilepilov.restfull.dto.UserDto;
import com.ilepilov.restfull.exception.UserServiceException;
import com.ilepilov.restfull.requestmodel.UserDetailsRequestModel;
import com.ilepilov.restfull.response.*;
import com.ilepilov.restfull.service.UserService;
import lombok.extern.slf4j.Slf4j;
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

    @RequestMapping(path = "/{id}",
            method = RequestMethod.GET,
            produces = {
                MediaType.APPLICATION_XML_VALUE,
                MediaType.APPLICATION_JSON_VALUE
            }
    )
    public UserRest getUser(@PathVariable("id") String id) {
        UserRest returnedUser = new UserRest();

        UserDto userDto = userService.getUserById(id);
        BeanUtils.copyProperties(userDto, returnedUser);

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
        UserRest userRest = new UserRest();

        if (userDetails.getEmail().isEmpty() || userDetails.getPassword().isEmpty()) {
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }

        if (userDetails.getFirstName().isEmpty()) {
            throw new NullPointerException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        userDto = userService.save(userDto);

        BeanUtils.copyProperties(userDto, userRest);

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
        UserRest userRest = new UserRest();

        if (userDetails.getEmail().isEmpty() || userDetails.getPassword().isEmpty()) {
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }

        if (userDetails.getFirstName().isEmpty()) {
            throw new NullPointerException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }

        UserDto updatedUser = new UserDto();
        BeanUtils.copyProperties(userDetails, updatedUser);

        updatedUser = userService.updateUser(id, updatedUser);
        BeanUtils.copyProperties(updatedUser, userRest);

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
            UserRest userRest = new UserRest();
            BeanUtils.copyProperties(userDto, userRest);
            usersRest.add(userRest);
        }

        return usersRest;
    }
}
