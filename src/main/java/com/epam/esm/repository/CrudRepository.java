package com.epam.esm.repository;

import java.util.Optional;

public interface CrudRepository <T,K>{
    T create(T t);

    public Optional<T> findById(K id);

    boolean delete(K id);

    T update(T obj);
}
