package com.epam.esm.controller;

import com.epam.esm.assembler.UserAssembler;
import com.epam.esm.dto.response.UserDtoResponse;
import com.epam.esm.service.UserService;
import com.epam.esm.util.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService service;

    private final UserAssembler assembler;

    @Autowired
    public UserController(UserService service, UserAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @GetMapping("/{id}")
    public UserDtoResponse find(@PathVariable int id) {
        return assembler.toModel(service.getUserById(id));
    }

    @GetMapping
    public List<UserDtoResponse> findAll(Pagination pagination) {
        return assembler.toCollectionModel(service.getAllUsers(pagination))
                .getContent().stream().toList();
    }
}
