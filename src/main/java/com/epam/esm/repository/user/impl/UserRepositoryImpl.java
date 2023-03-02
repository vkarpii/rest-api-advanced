package com.epam.esm.repository.user.impl;

import com.epam.esm.entity.User;
import com.epam.esm.repository.user.UserRepository;
import com.epam.esm.util.Pagination;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.epam.esm.repository.user.util.SQLQuery.*;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public User create(User user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<User> findById(Integer id) {
        return Optional.ofNullable(
                manager.find(User.class,id));
    }

    @Override
    public boolean delete(Integer id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public User update(User obj) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<User> getAll(Pagination pagination) {
        return manager.createQuery(FIND_ALL_USERS,User.class)
                .setFirstResult(pagination.getOffsetPosition())
                .setMaxResults(pagination.getSize())
                .getResultList();
    }
}
