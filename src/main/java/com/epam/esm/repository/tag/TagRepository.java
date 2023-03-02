package com.epam.esm.repository.tag;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.CrudRepository;
import com.epam.esm.util.Pagination;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;
import java.util.Set;


public interface TagRepository extends CrudRepository<Tag, Integer> {

    public Optional<Tag> isExist(String tagName);

    public List<Tag> getAll(Pagination pagination);

}
