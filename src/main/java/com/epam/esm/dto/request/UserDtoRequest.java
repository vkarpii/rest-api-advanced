package com.epam.esm.dto.request;

import lombok.Data;

@Data
public class UserDtoRequest {

    private String firstName;

    private String lastName;

    private String email;

    private String login;

    private String password;
}
