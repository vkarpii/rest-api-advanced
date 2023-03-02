package com.epam.esm.repository.certificate;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.CrudRepository;
import com.epam.esm.util.Pagination;

import java.util.List;
import java.util.Optional;

public interface CertificateRepository extends CrudRepository<GiftCertificate, Integer> {
    List<GiftCertificate> getAll(Pagination pagination,String s);

    Optional<GiftCertificate> findByName(String certificateName);
}
