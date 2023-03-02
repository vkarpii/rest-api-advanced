package com.epam.esm.service.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ApplicationException;
import com.epam.esm.repository.certificate.CertificateRepository;
import com.epam.esm.repository.tag.TagRepository;
import com.epam.esm.util.Pagination;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;


import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class TagServiceImplTest {

    private final String DEFAULT_NAME = "Tag";

    private final int DEFAULT_ID = 1;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private MessageSource messageSource;

    @Mock
    private CertificateRepository certificateDao;

    @InjectMocks
    private TagServiceImpl tagService;

    @Test
    void findTagByIdTest() {
        Tag tag = new Tag(DEFAULT_ID, DEFAULT_NAME);

        Mockito.when(tagRepository.findById(DEFAULT_ID)).thenReturn(Optional.of(tag));
        Tag resultTag = tagService.getTagById(DEFAULT_ID);

        assertEquals(tag, resultTag);
    }

    @Test
    void findAllTagsTest() {
        Tag tag = new Tag(DEFAULT_NAME);
        List<Tag> allTags = Collections.singletonList(tag);
        List<Tag> expectedTags = Collections.singletonList(tag);

        Pagination pagination = new Pagination(1,10);

        Mockito.when(tagRepository.getAll(pagination)).thenReturn(allTags);
        List<Tag> resultTagList = tagService.getAllTags(pagination);

        assertEquals(expectedTags, resultTagList);
    }

    @Test
    void deleteTagTest() {
        Mockito.when(tagRepository.delete(DEFAULT_ID)).thenReturn(Boolean.TRUE);
        Mockito.when(tagRepository.findById(DEFAULT_ID)).thenReturn(Optional.of(new Tag(DEFAULT_ID,DEFAULT_NAME)));

        Assertions.assertEquals(Boolean.TRUE, tagService.deleteTagById(DEFAULT_ID));
    }

    @Test
    void createTagTest() {
        Tag tagForCreating = new Tag(DEFAULT_NAME);
        Tag createdTag = new Tag(DEFAULT_ID, DEFAULT_NAME);

        Mockito.when(tagRepository.create(tagForCreating)).thenReturn(createdTag);
        Tag resultTag = tagService.createNewTag(tagForCreating);

        assertEquals(createdTag, resultTag);
    }

    @Test
    void createTagTestShouldThrowException() {
        Tag tagForCreating = new Tag(DEFAULT_NAME);
        Tag existingTag = new Tag(DEFAULT_ID, DEFAULT_NAME);

        Mockito.when(tagRepository.isExist(DEFAULT_NAME)).thenReturn(Optional.of(existingTag));

        assertThrows(ApplicationException.class, () -> tagService.createNewTag(tagForCreating));
    }

    @Test
    void deleteTagTestShouldThrowException() {
        int id = 99;
        assertThrows(ApplicationException.class, () -> tagService.deleteTagById(id));
    }

    @Test
    void GetFullDataTagsTest() {
        List<Tag> tags = List.of(new Tag(DEFAULT_NAME));

        Mockito.when(tagRepository.isExist(Mockito.anyString())).thenReturn(Optional.of(new Tag(DEFAULT_NAME)));
        //Mockito.when(tagDao.create(Mockito.any())).thenReturn(new Tag("Tag1"));
        List<Tag> getTags = tagService.getFullTagsData(tags);
        assertEquals(tags.size(), getTags.size());
    }


}
