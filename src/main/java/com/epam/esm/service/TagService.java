package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ApplicationException;
import com.epam.esm.util.Pagination;

import java.util.List;
import java.util.Set;

/**
 * This interface represents Service implementation .
 *
 * @param <T> has to implement {@link Tag} interface
 * @author Vitaly Karpii
 * @see Tag
 */
public interface TagService {

    /**
     * This method create new tag.
     *
     * @throws {@link ApplicationException} in case if this tag's name already exist.
     */
    public Tag createNewTag(Tag tag);

    /**
     * This method delete tag by his id.
     *
     * @throws {@link ApplicationException} in case if this tag's id not found.
     */
    public boolean deleteTagById(int id);

    /**
     * This method return tag by his id.
     *
     * @return {@link Tag}
     * @throws {@link ApplicationException} in case if tag not found with searched id.
     */
    public Tag getTagById(int id);

    /**
     * This method return all existing tags.
     *
     * @return list of{@link Tag}
     */
    public List<Tag> getAllTags(Pagination pagination);

    /**
     * This method update all existing tags by id.
     *
     * @return list of{@link Tag}
     * @throws {@link ApplicationException} in case if this tag's id not found.
     */
    List<Tag> getFullTagsData(List<Tag> tags);
}
