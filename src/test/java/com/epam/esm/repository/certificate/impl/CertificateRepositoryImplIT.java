package com.epam.esm.repository.certificate.impl;

import com.epam.esm.config.TestConfig;
import com.epam.esm.dto.mapper.impl.GiftCertificateMapper;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.certificate.CertificateRepository;
import com.epam.esm.repository.tag.TagRepository;
import com.epam.esm.util.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

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
class CertificateRepositoryImplIT {

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private GiftCertificateMapper certificateMapper;

    @Autowired
    private TestEntityManager entityManager;

    private final String CERTIFICATE_NAME_1 = "Certificate-1";

    private final String CERTIFICATE_NAME_2 = "Certificate-2";

    @BeforeEach
    public void setUp() {
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        List<Tag> tags = new ArrayList<>(
                List.of(new Tag("Tag-1"), new Tag("Tag-2"), new Tag("Tag-3")));
        GiftCertificate certificate = new GiftCertificate(CERTIFICATE_NAME_1,
                "Description-1",
                BigDecimal.valueOf(199),
                90,
                timestamp,timestamp,
                tags);
        entityManager.persist(certificate);
        timestamp = Timestamp.valueOf(LocalDateTime.now());
        certificate = new GiftCertificate(CERTIFICATE_NAME_2,
                "Description-2",
                BigDecimal.valueOf(99),
                60,
                timestamp,timestamp,
                tags);
        entityManager.persist(certificate);
        entityManager.flush();
    }

    @Test
    void create() {
        int sizeBeforeCreate = certificateRepository.getAll(getPagination(),"").size();
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        certificateRepository.create(new GiftCertificate("NEW",
                "Description-NEW",
                BigDecimal.valueOf(1199),
                91,
                timestamp,timestamp,
                List.of(new Tag("Tag-10"))));
        int sizeAfterCreate = certificateRepository.getAll(getPagination(),"").size();
        assertEquals(sizeBeforeCreate + 1 ,sizeAfterCreate);
    }

    @Test
    void findById() {
        GiftCertificate find = certificateRepository.findByName(CERTIFICATE_NAME_1).get();
        GiftCertificate certificate = certificateRepository.findById(find.getId()).get();
        assertEquals(find,certificate);
    }

    @Test
    void delete() {
        int sizeBeforeDelete = certificateRepository.getAll(getPagination(),"").size();
        int id = certificateRepository.findByName(CERTIFICATE_NAME_2).get().getId();
        certificateRepository.delete(id);
        int sizeAfterDelete = certificateRepository.getAll(getPagination(),"").size();
        assertEquals(sizeBeforeDelete - 1 ,sizeAfterDelete);
    }

    @Test
    void update() {
        GiftCertificate certificate = certificateRepository.findByName(CERTIFICATE_NAME_1).get();
        GiftCertificate changed = new GiftCertificate();
        changed.setCertificateDescription("desc");
        certificate = certificateMapper.merge(certificate,changed);
        GiftCertificate updated = certificateRepository.update(certificate);
        assertEquals(certificate,updated);
        assertEquals("desc",updated.getCertificateDescription());
    }

    @Test
    void getAll() {
        int size = certificateRepository.getAll(getPagination(),"").size();
        assertEquals(2,size);
    }

    @Test
    void findByName() {
        GiftCertificate certificate = certificateRepository.findByName(CERTIFICATE_NAME_1).get();
        assertEquals(CERTIFICATE_NAME_1,certificate.getCertificateName());
        certificate = certificateRepository.findByName(CERTIFICATE_NAME_2).get();
        assertEquals(CERTIFICATE_NAME_2,certificate.getCertificateName());
    }

    @Test
    void canNotFindByName() {
        assertEquals(Optional.empty(),certificateRepository.findByName("Dont`exist"));
    }

    private Pagination getPagination(){
        return new Pagination(1,10);
    }
}