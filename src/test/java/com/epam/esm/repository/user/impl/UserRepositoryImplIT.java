package com.epam.esm.repository.user.impl;

import com.epam.esm.config.TestConfig;
import com.epam.esm.entity.User;
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

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(TestConfig.class)
class UserRepositoryImplIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    private int userId;

    private final String USER_EMAIL = "email-1@gmail.com";

    @BeforeEach
    public void setUp() {
        User user = new User("Name-1","Surname-1",USER_EMAIL,"login","");
        entityManager.persist(user);
        userId = (int) entityManager.getId(user);
        entityManager.persist(new User("Name-2","Surname-2","email-2@gmail.com","login2",""));
        entityManager.flush();
    }

    @Test
    void create() {
        assertThrows(UnsupportedOperationException.class,() -> {
            userRepository.create(new User());
        });
    }

    @Test
    void findById() {
        User user = userRepository.findById(userId).get();
        assertEquals(userId,user.getId());
        assertEquals(USER_EMAIL,user.getEmail());
    }

    @Test
    void delete() {
        assertThrows(UnsupportedOperationException.class,() -> {
            userRepository.delete(1);
        });
    }

    @Test
    void update() {
        assertThrows(UnsupportedOperationException.class,() -> {
            userRepository.update(new User());
        });
    }

    @Test
    void getAll() {
        int size =userRepository.getAll(getPagination()).size();
        assertEquals(2,size);
    }

    private Pagination getPagination(){
        return new Pagination(1,10);
    }
}