package com.epam.esm.repository.order;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.CrudRepository;
import com.epam.esm.util.Pagination;

import java.util.List;
import java.util.Optional;


public interface OrderRepository extends CrudRepository<Order,Integer> {
    List<Order> findAll(int id, Pagination pagination);

    Optional<Tag> mostWidelyUsedTag(int id);
}
