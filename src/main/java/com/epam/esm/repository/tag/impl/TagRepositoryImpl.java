package com.epam.esm.repository.tag.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.tag.TagRepository;
import com.epam.esm.util.Pagination;
import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.epam.esm.repository.tag.util.SQLQuery.*;

@Slf4j
@Repository
public class TagRepositoryImpl implements TagRepository {

    @PersistenceContext
    private EntityManager manager;


    @Override
    public Tag create(Tag tag) {
        manager.persist(tag);
        return tag;
    }

    @Override
    public Optional<Tag> findById(Integer id) {
        return Optional.ofNullable(
                manager.find(Tag.class,id));
    }

    @Override
    public boolean delete(Integer id) {
        Tag tag = manager.find(Tag.class,id);
        manager.remove(tag);
        return true;
    }

    @Override
    public Tag update(Tag tag) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Tag> isExist(String tagName) {
        try {
            return Optional.ofNullable(manager.createQuery(TAG_IS_EXISTS, Tag.class)
                    .setParameter(PARAM_TAG_NAME, tagName)
                    .getSingleResult());
        } catch (NoResultException e){
            return Optional.empty();
        }
    }

    @Override
    public List<Tag> getAll(Pagination pagination) {
        return manager.createQuery(FIND_ALL_TAGS,Tag.class)
                .setFirstResult(pagination.getOffsetPosition())
                .setMaxResults(pagination.getSize())
                .getResultList();
    }


}
