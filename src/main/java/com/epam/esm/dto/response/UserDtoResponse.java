package com.epam.esm.dto.response;

import com.epam.esm.entity.Tag;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class UserDtoResponse extends RepresentationModel<Tag> {
    private int id;

    private String firstName;

    private String lastName;

    private String email;

    private String login;
}
