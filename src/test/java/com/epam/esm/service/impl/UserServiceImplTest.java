package com.epam.esm.service.impl;

import com.epam.esm.dto.mapper.impl.UserMapper;
import com.epam.esm.dto.response.UserDtoResponse;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ApplicationException;
import com.epam.esm.repository.user.UserRepository;
import com.epam.esm.util.Pagination;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    private final String DEFAULT_NAME = "User";

    private final int DEFAULT_ID = 1;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MessageSource messageSource;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void findUserByIdTest() {
        User user = new User();

        Mockito.when(userRepository.findById(DEFAULT_ID)).thenReturn(Optional.of(user));
        UserDtoResponse resultUser = userService.getUserById(DEFAULT_ID);

        assertEquals(userMapper.toDTO(user), resultUser);
    }

    @Test
    void findUserByIdShouldThrowException() {
        Mockito.when(userRepository.findById(DEFAULT_ID)).thenReturn(Optional.empty());

        assertThrows(ApplicationException.class, () -> userService.getUserById(DEFAULT_ID));
    }

    @Test
    void findAllUsersTest() {
        Pagination pagination = new Pagination(1,10);
        List<User> users = List.of(new User());

        Mockito.when(userRepository.getAll(pagination)).thenReturn(users);
        List<UserDtoResponse> responses = userService.getAllUsers(pagination);

        assertEquals(users.size(), responses.size());
    }

}
