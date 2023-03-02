package com.epam.esm.service.impl;

import com.epam.esm.dto.mapper.impl.GiftCertificateMapper;
import com.epam.esm.dto.request.GiftCertificateDtoRequest;
import com.epam.esm.dto.response.GiftCertificateDtoResponse;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ApplicationException;
import com.epam.esm.exception.ExceptionMessage;
import com.epam.esm.repository.certificate.CertificateRepository;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.util.QueryBuilder;
import com.epam.esm.util.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Slf4j
@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final QueryBuilder builder;
    private final CertificateRepository repository;

    private final GiftCertificateMapper certificateMapper;
    private final TagService tagService;

    private final MessageSource messageSource;

    @Autowired
    public GiftCertificateServiceImpl(QueryBuilder builder, CertificateRepository repository, GiftCertificateMapper certificateMapper, TagService tagService, MessageSource messageSource) {
        this.builder = builder;
        this.repository = repository;
        this.certificateMapper = certificateMapper;
        this.tagService = tagService;
        this.messageSource = messageSource;
    }

    private GiftCertificate getCertificateById(int id){
        return repository.findById(id).orElseThrow(() -> {
            log.error(messageSource.getMessage(ExceptionMessage.CERTIFICATE_NOT_FOUND, new Object[]{},
                    LocaleContextHolder.getLocale()));
            return new ApplicationException(ExceptionMessage.CERTIFICATE_NOT_FOUND);
        });
    }

    @Override
    @Transactional
    public GiftCertificateDtoResponse getCertificateDtoById(int id) {
        return certificateMapper.toDTO(getCertificateById(id));
    }

    @Override
    @Transactional
    public GiftCertificateDtoResponse createNewCertificate(GiftCertificateDtoRequest certificateDTO) {
        GiftCertificate certificate = certificateMapper.toEntity(certificateDTO);
        if (repository.findByName(certificate.getCertificateName()).isPresent()) {
            log.error(messageSource.getMessage(ExceptionMessage.CERTIFICATE_IS_ALREADY_EXISTS, new Object[]{},
                    LocaleContextHolder.getLocale()));
            throw new ApplicationException(ExceptionMessage.CERTIFICATE_IS_ALREADY_EXISTS);
        }
        setNewCreateDate(certificate);
        setNewLastUpdateDate(certificate);
        mergeTagsWithDb(certificate);
        GiftCertificate createdCertificate = repository.create(certificate);
        return certificateMapper.toDTO(createdCertificate);
    }

    @Override
    public GiftCertificateDtoResponse updateCertificate(int id, GiftCertificateDtoRequest certificateDto) {
        GiftCertificate certificate = certificateMapper.toEntity(certificateDto);
        GiftCertificate current = getCertificateById(id);
        mergeTagsWithDb(certificate);
        current = certificateMapper.merge(current, certificate);
        repository.update(current);
        return certificateMapper.toDTO(current);
    }

    private void mergeTagsWithDb(GiftCertificate certificate){
        if(nonNull(certificate.getTags())){
            List<Tag> tags = tagService.getFullTagsData(certificate.getTags());
            certificate.setTags(tags);
        }
    }

    private void setNewLastUpdateDate(GiftCertificate certificate) {
        certificate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
    }

    private void setNewCreateDate(GiftCertificate certificate) {
        certificate.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
    }

    @Override
    public List<GiftCertificateDtoResponse> getCertificates(Pagination pagination,Map<String, String> params) {
        List<GiftCertificate> certificates = repository.getAll(pagination,builder.buildQuery(params));
        return certificates.stream().map(certificateMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean deleteCertificateById(int id) {
        return getCertificateById(id).getId() == id && repository.delete(id);
    }

    @Override
    public List<GiftCertificate> getFullCertificatesData(List<GiftCertificate> certificates) {
        List<GiftCertificate> updated = new ArrayList<>();
        certificates.forEach(certificate -> updated.add(getCertificateById(certificate.getId())));
        return updated;
    }

    @Override
    public GiftCertificateDtoResponse changePrice(int id, GiftCertificateDtoRequest request) {
        BigDecimal price = request.getPrice();
        if (price.compareTo(BigDecimal.ZERO) < 0){
            log.error(messageSource.getMessage(ExceptionMessage.WRONG_PRICE, new Object[]{},
                    LocaleContextHolder.getLocale()));
            throw new ApplicationException(ExceptionMessage.WRONG_PRICE);
        }
        GiftCertificate certificate = getCertificateById(id);
        certificate.setPrice(price);
        return certificateMapper.toDTO(repository.update(certificate));
    }
    @Override
    public GiftCertificateDtoResponse changeDuration(int id, GiftCertificateDtoRequest request) {
        int duration = request.getDurationInDays();
        if (duration <= 0){
            log.error(messageSource.getMessage(ExceptionMessage.WRONG_DURATION, new Object[]{},
                    LocaleContextHolder.getLocale()));
            throw new ApplicationException(ExceptionMessage.WRONG_DURATION);
        }
        GiftCertificate certificate = getCertificateById(id);
        certificate.setDuration(duration);
        return certificateMapper.toDTO(repository.update(certificate));
    }

    @Override
    public List<GiftCertificate> getGiftCertificatesWithIds(List<Integer> ids){
        List<GiftCertificate> certificates = new ArrayList<>();
        ids.forEach(certificateId -> {
            GiftCertificate certificate = new GiftCertificate();
            certificate.setId(certificateId);
            certificates.add(certificate);
        });
        return certificates;
    }

}
