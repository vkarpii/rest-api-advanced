package com.epam.esm.repository.user;

import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.repository.CrudRepository;
import com.epam.esm.util.Pagination;

import java.util.List;

public interface UserRepository extends CrudRepository<User,Integer> {

    public List<User> getAll(Pagination pagination);
}
