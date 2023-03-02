package com.epam.esm.service;

import com.epam.esm.dto.response.GiftCertificateDtoResponse;
import com.epam.esm.dto.response.UserDtoResponse;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ApplicationException;
import com.epam.esm.util.Pagination;

import java.util.List;

/**
 * This interface represents Service implementation that connected controller with Data Access Object.
 *
 * @param <T> has to implement {@link User} interface
 * @author Vitaly Karpii
 * @see User
 */
public interface UserService {

    /**
     * This method return user by his id.
     *
     * @return {@link UserDtoResponse}
     * @throws {@link ApplicationException} in case if tag not found with searched id.
     */
    UserDtoResponse getUserById(int id);

    /**
     * This method return all active users with criteria.
     *
     * @return list of{@link UserDtoResponse}
     */
    List<UserDtoResponse> getAllUsers(Pagination pagination);

    /**
     * This method return user by his id.
     *
     * @return {@link User}
     * @throws {@link ApplicationException} in case if tag not found with searched id.
     */
    User getUser(int id);
}
