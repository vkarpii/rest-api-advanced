package com.epam.esm.service.impl;

import com.epam.esm.dto.mapper.impl.UserMapper;
import com.epam.esm.dto.response.UserDtoResponse;

import com.epam.esm.entity.User;
import com.epam.esm.exception.ApplicationException;
import com.epam.esm.exception.ExceptionMessage;
import com.epam.esm.repository.user.UserRepository;
import com.epam.esm.service.UserService;
import com.epam.esm.util.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    private final UserMapper userMapper;

    private final MessageSource messageSource;

    @Autowired
    public UserServiceImpl(UserRepository repository, UserMapper userMapper, MessageSource messageSource) {
        this.repository = repository;
        this.userMapper = userMapper;
        this.messageSource = messageSource;
    }

    @Override
    public UserDtoResponse getUserById(int id) {
        return userMapper.toDTO(getUser(id));
    }

    @Override
    public List<UserDtoResponse> getAllUsers(Pagination pagination) {
        List<User> users = repository.getAll(pagination);
        return users.stream().map(userMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public User getUser(int id) {
        return repository.findById(id).orElseThrow(() -> {
            log.error(messageSource.getMessage(ExceptionMessage.USER_NOT_FOUND, new Object[]{},
                    LocaleContextHolder.getLocale()));
            return new ApplicationException(ExceptionMessage.USER_NOT_FOUND);
        });
    }
}
