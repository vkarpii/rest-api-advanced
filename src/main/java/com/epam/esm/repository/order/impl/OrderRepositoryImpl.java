package com.epam.esm.repository.order.impl;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.order.OrderRepository;
import com.epam.esm.util.Pagination;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.epam.esm.repository.order.util.SQLQuery.*;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<Order> findAll(int id, Pagination pagination) {
        return manager.createQuery(FIND_ALL_ORDERS, Order.class)
                .setParameter(PARAM_ID,id)
                .setFirstResult(pagination.getOffsetPosition())
                .setMaxResults(pagination.getSize())
                .getResultList();
    }

    @Override
    public Optional<Tag> mostWidelyUsedTag(int userId) {
        try{
            return Optional.ofNullable(manager.createQuery(MOST_WIDELY_USED_TAG, Tag.class)
                    .setParameter(PARAM_ID, userId)
                    .getSingleResult());
        } catch (NoResultException e){
            return Optional.empty();
        }
    }

    @Override
    public Order create(Order order) {
        order.getCertificates().forEach(orderGiftCertificate -> {
            manager.persist(orderGiftCertificate);
        });
        manager.persist(order);
        return order;
    }

    @Override
    public Optional<Order> findById(Integer id) {
        return Optional.ofNullable(manager.find(Order.class,id));
    }

    @Override
    public boolean delete(Integer id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Order update(Order obj) {
        throw new UnsupportedOperationException();
    }

}
