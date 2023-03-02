package com.epam.esm.repository.order.impl;

import com.epam.esm.config.TestConfig;
import com.epam.esm.entity.*;
import com.epam.esm.repository.order.OrderRepository;
import com.epam.esm.repository.user.UserRepository;
import com.epam.esm.util.Pagination;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(TestConfig.class)
class OrderRepositoryImplIT {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    private int userId;

    private final String MOST_WIDELY_USED_TAG = "Tag-1";

    @BeforeEach
    public void setUp() {
        User user = new User("Name-1","Surname-1","email-1@gmail.com","","");
        entityManager.persist(user);
        user.setId(((Integer)entityManager.getId(user)));
        userId = user.getId();
        /*OrderDetails details = new OrderDetails(BigDecimal.valueOf(124), Timestamp.valueOf(LocalDateTime.now()));
        entityManager.persist(details);
        details.setId((Integer) entityManager.getId(details));*/
        List<OrderGiftCertificate> certificates = new ArrayList<>();
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());

        List<Tag> tags = List.of(new Tag(MOST_WIDELY_USED_TAG),new Tag("Tag-2"),
                new Tag("Tag-3"));
        tags.forEach(tag -> {
            entityManager.persist(tag);
            tag.setId((Integer) entityManager.getId(tag));
        });
        certificates.add(new OrderGiftCertificate("Certificate-1","Description-1",BigDecimal.valueOf(12),
                timestamp,timestamp,List.of(tags.get(0),tags.get(1))));
        certificates.add(new OrderGiftCertificate("Certificate-2","Description-2",BigDecimal.valueOf(112),
                timestamp,timestamp,List.of(tags.get(0),tags.get(2))));
        Order order = new Order(Timestamp.valueOf(LocalDateTime.now()),user,certificates);
        entityManager.persist(order);
        entityManager.flush();
    }

    @Test
    void findAll() {
        int size = orderRepository.findAll(userId,getPagination()).size();
        assertEquals(1,size);
    }

    @Test
    void mostWidelyUsedTag() {
        Tag tag = orderRepository.mostWidelyUsedTag(userId).get();
        assertEquals(MOST_WIDELY_USED_TAG,tag.getTagName());
    }

    @Test
    void notFoundMostWidelyUsedTag() {
        User user = new User("Name-0","Surname-0","email-0@gmail.com","Login","123");
        entityManager.persist(user);
        int id = (int) entityManager.getId(user);
        assertEquals(Optional.empty(),orderRepository.mostWidelyUsedTag(id));
    }

    @Test
    void create() {
        int sizeBeforeCreate = orderRepository.findAll(userId,getPagination()).size();
        Order order = new Order();
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        OrderGiftCertificate orderCertificate = new OrderGiftCertificate("Name","Desc",BigDecimal.valueOf(100),
                timestamp,timestamp,List.of());
        order.setCertificates(List.of(orderCertificate));
        order.setPurchaseDate(Timestamp.valueOf(LocalDateTime.now()));
        order.setUser(userRepository.findById(userId).get());
        orderRepository.create(order);
        int sizeAfterCreate = orderRepository.findAll(userId,getPagination()).size();
        assertEquals(sizeBeforeCreate + 1, sizeAfterCreate);
    }

    @Test
    void findById() {
        Order order = orderRepository.findAll(userId,getPagination()).get(0);
        Order findById = orderRepository.findById(order.getId()).get();
        assertEquals(order,findById);
    }

    @Test
    void delete() {
        assertThrows(UnsupportedOperationException.class,() -> {
           orderRepository.delete(1);
        });
    }

    @Test
    void update() {
        assertThrows(UnsupportedOperationException.class,() -> {
            orderRepository.update(new Order());
        });
    }
    private Pagination getPagination(){
        return new Pagination(1,10);
    }
}