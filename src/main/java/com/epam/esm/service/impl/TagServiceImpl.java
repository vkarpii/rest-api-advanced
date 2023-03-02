package com.epam.esm.service.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ApplicationException;
import com.epam.esm.exception.ExceptionMessage;
import com.epam.esm.repository.tag.TagRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.util.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TagServiceImpl implements TagService {

    private final TagRepository repository;

    private final MessageSource messageSource;

    @Autowired
    public TagServiceImpl(TagRepository repository, MessageSource messageSource) {
        this.repository = repository;
        this.messageSource = messageSource;
    }

    @Override
    public List<Tag> getAllTags(Pagination pagination) {
        return repository.getAll(pagination);
    }

    @Override
    @Transactional
    public List<Tag> getFullTagsData(List<Tag> tags) {
        List<Tag> updatedTags = new ArrayList<>();
        tags.forEach(tag -> {
            if (repository.isExist(tag.getTagName()).isPresent()){
                tag = repository.isExist(tag.getTagName()).get();
            }
            updatedTags.add(tag);
        });
        return updatedTags;
    }

    @Override
    @Transactional
    public Tag createNewTag(Tag tag) {
        if (repository.isExist(tag.getTagName()).isPresent()) {
            log.error(messageSource.getMessage(ExceptionMessage.TAG_IS_ALREADY_EXISTS, new Object[]{},
                    LocaleContextHolder.getLocale()));
            throw new ApplicationException(ExceptionMessage.TAG_IS_ALREADY_EXISTS);
        }
        return repository.create(tag);
    }

    @Override
    @Transactional
    public boolean deleteTagById(int id) {
        return getTagById(id).getId() == id && repository.delete(id);
    }

    @Override
    public Tag getTagById(int id) {
        return repository.findById(id).orElseThrow(() -> {
            log.error(messageSource.getMessage(ExceptionMessage.TAG_NOT_FOUND, new Object[]{},
                    LocaleContextHolder.getLocale()));
            return new ApplicationException(ExceptionMessage.TAG_NOT_FOUND);
        });
    }
}
