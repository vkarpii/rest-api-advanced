package com.epam.esm.dto.mapper.impl;

import com.epam.esm.dto.mapper.DtoMapper;
import com.epam.esm.dto.request.UserDtoRequest;
import com.epam.esm.dto.response.UserDtoResponse;
import com.epam.esm.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements DtoMapper<UserDtoResponse, UserDtoRequest, User> {
    @Override
    public UserDtoResponse toDTO(User user) {
        UserDtoResponse response = new UserDtoResponse();
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setLogin(user.getLogin());
        return response;
    }

    @Override
    public User toEntity(UserDtoRequest userDtoRequest) {
        User user = new User();
        user.setFirstName(userDtoRequest.getFirstName());
        user.setLastName(userDtoRequest.getLastName());
        user.setEmail(userDtoRequest.getEmail());
        user.setLogin(userDtoRequest.getLogin());
        user.setPassword(userDtoRequest.getPassword());
        return user;
    }
}
