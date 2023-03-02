package com.epam.esm.controller;

import com.epam.esm.assembler.TagAssembler;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.util.Pagination;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/tag")
public class TagController {
    private final TagService service;
    private final TagAssembler assembler;

    @Autowired
    public TagController(TagService service, TagAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @PostMapping
    public Tag create(@RequestBody @Valid Tag tag) {
        return assembler.toModel(service.createNewTag(tag));
    }

    @GetMapping("/{id}")
    public Tag find(@PathVariable int id) {
        return assembler.toModel(service.getTagById(id));
    }

    @GetMapping
    public List<Tag> findAll(Pagination pagination) {
        return assembler.toCollectionModel(service.getAllTags(pagination))
                .getContent().stream().toList();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        service.deleteTagById(id);
    }
}
