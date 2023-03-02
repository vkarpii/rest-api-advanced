package com.epam.esm.repository.tag.impl;

import com.epam.esm.config.TestConfig;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.tag.TagRepository;
import com.epam.esm.util.Pagination;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(TestConfig.class)
class TagRepositoryImplIT {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TestEntityManager entityManager;

    private final String DEFAULT_NAME_1 = "Tag-1";
    private final String DEFAULT_NAME_2 = "Tag-2";

    private final String BAD_NAME = "Tag-3";

    @BeforeEach
    public void setUp() {
        entityManager.persist(new Tag(DEFAULT_NAME_1));
        entityManager.persist(new Tag(DEFAULT_NAME_2));
        entityManager.flush();
    }


    @Test
    void create() {
        int sizeBeforeCreate = tagRepository.getAll(getPagination()).size();
        Tag tag = new Tag(BAD_NAME);
        tagRepository.create(tag);
        int sizeAfterCreate = tagRepository.getAll(getPagination()).size();

        assertEquals(sizeBeforeCreate + 1,sizeAfterCreate);
    }

    @Test
    void findById() {
        int id = tagRepository.isExist(DEFAULT_NAME_1).get().getId();
        Tag tag = tagRepository.findById(id).get();
        assertEquals(DEFAULT_NAME_1,tag.getTagName());
    }

    @Test
    void delete() {
        int sizeBeforeDelete = tagRepository.getAll(getPagination()).size();
        Tag tag = tagRepository.isExist(DEFAULT_NAME_1).get();
        tagRepository.delete(tag.getId());
        int sizeAfterDelete = tagRepository.getAll(getPagination()).size();

        assertEquals(sizeBeforeDelete - 1,sizeAfterDelete);
    }

    @Test
    void update() {
        assertThrows(UnsupportedOperationException.class,() -> {
            Tag tag = tagRepository.isExist(DEFAULT_NAME_2).get();
            tag.setTagName(BAD_NAME);
            tagRepository.update(tag);
        });
    }

    @Test
    void isExist() {
        assertTrue(tagRepository.isExist(DEFAULT_NAME_1).isPresent());
        assertTrue(tagRepository.isExist(DEFAULT_NAME_2).isPresent());
        assertFalse(tagRepository.isExist(BAD_NAME).isPresent());
    }

    @Test
    void getAll() {
        List<Tag> tags = tagRepository.getAll(getPagination());
        assertEquals(2,tags.size());
    }

    private Pagination getPagination(){
        return new Pagination(1,10);
    }
}