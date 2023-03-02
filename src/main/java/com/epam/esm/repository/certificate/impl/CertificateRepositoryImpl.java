package com.epam.esm.repository.certificate.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.certificate.CertificateRepository;
import com.epam.esm.util.Pagination;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.epam.esm.repository.certificate.util.SQLQuery.*;

@Slf4j
@Repository
public class CertificateRepositoryImpl implements CertificateRepository {

    @PersistenceContext
    private EntityManager manager;


    @Override
    public GiftCertificate create(GiftCertificate certificate) {
        return manager.merge(certificate);
    }

    @Override
    public Optional<GiftCertificate> findById(Integer id) {
        return Optional.ofNullable(manager.find(GiftCertificate.class,id));
    }

    @Override
    public boolean delete(Integer id) {
        GiftCertificate certificate = manager.find(GiftCertificate.class,id);
        manager.remove(certificate);
        return true;
    }

    @Override
    @Transactional
    public GiftCertificate update(GiftCertificate certificate) {
        return manager.merge(certificate);
    }

    @Override
    public List<GiftCertificate> getAll(Pagination pagination, String condition) {
        return manager.createQuery(GET_ALL + condition, GiftCertificate.class)
                .setFirstResult(pagination.getOffsetPosition())
                .setMaxResults(pagination.getSize())
                .getResultList();
    }


    @Override
    public Optional<GiftCertificate> findByName(String certificateName) {
        try {
            return Optional.ofNullable(manager.createQuery(GET_CERTIFICATE_BY_NAME, GiftCertificate.class)
                    .setParameter(PARAM_CERTIFICATE_NAME, certificateName)
                    .getSingleResult());
        } catch (NoResultException e){
            return Optional.empty();
        }
    }
}
