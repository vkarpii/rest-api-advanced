package com.epam.esm.dto.mapper.impl;

import com.epam.esm.dto.mapper.DtoMapper;
import com.epam.esm.dto.request.GiftCertificateDtoRequest;
import com.epam.esm.dto.response.GiftCertificateDtoResponse;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.OrderGiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.util.IsoDateFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNullElse;

@Component
public class GiftCertificateMapper implements DtoMapper<GiftCertificateDtoResponse, GiftCertificateDtoRequest, GiftCertificate> {

    private final IsoDateFormatter dateFormatter;

    @Autowired
    public GiftCertificateMapper(IsoDateFormatter dateFormatter) {
        this.dateFormatter = dateFormatter;
    }

    @Override
    public GiftCertificateDtoResponse toDTO(GiftCertificate certificate) {
        GiftCertificateDtoResponse giftCertificateDto = new GiftCertificateDtoResponse();
        giftCertificateDto.setId(certificate.getId());
        giftCertificateDto.setName(certificate.getCertificateName());
        giftCertificateDto.setDescription(certificate.getCertificateDescription());
        giftCertificateDto.setPrice(certificate.getPrice());
        giftCertificateDto.setTags(certificate.getTags());
        giftCertificateDto.setCreateDate(dateFormatter.convertTimesTampToISOFormat(certificate.getCreateDate()));
        giftCertificateDto.setLastUpdateDate(
                dateFormatter.convertTimesTampToISOFormat(certificate.getLastUpdateDate()));
        giftCertificateDto.setDurationInDays(certificate.getDuration());
        return giftCertificateDto;
    }

    @Override
    public GiftCertificate toEntity(GiftCertificateDtoRequest certificateDto) {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setCertificateName(certificateDto.getName());
        giftCertificate.setCertificateDescription(certificateDto.getDescription());
        giftCertificate.setPrice(certificateDto.getPrice());
        if (nonNull(certificateDto.getTags())) {
            giftCertificate.setTags(certificateDto.getTags().stream().map(Tag::new).collect
                    (Collectors.toList()));
        }
        giftCertificate.setDuration(certificateDto.getDurationInDays());
        return giftCertificate;
    }

    public GiftCertificate merge(GiftCertificate mergedCertificate, GiftCertificate certificate) {
        GiftCertificate finalCertificate = new GiftCertificate();
        finalCertificate.setId(mergedCertificate.getId());
        finalCertificate.setCertificateName(requireNonNullElse(certificate.getCertificateName(),
                mergedCertificate.getCertificateName()));
        finalCertificate.setCertificateDescription(requireNonNullElse(certificate.getCertificateDescription(),
                mergedCertificate.getCertificateDescription()));
        finalCertificate.setPrice(requireNonNullElse(certificate.getPrice(), mergedCertificate.getPrice()));
        finalCertificate.setDuration(certificate.getDuration());
        finalCertificate.setCreateDate(requireNonNullElse(certificate.getCreateDate(),
                mergedCertificate.getCreateDate()));
        finalCertificate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
        finalCertificate.setTags(requireNonNullElse(certificate.getTags(), mergedCertificate.getTags()));
        return finalCertificate;
    }

    public List<OrderGiftCertificate> orderTransform(List<GiftCertificate> certificates){
        List<OrderGiftCertificate> orderCertificates = new ArrayList<>();
        certificates.forEach(certificate -> {
            OrderGiftCertificate orderCertificate = new OrderGiftCertificate();
            orderCertificate.setName(certificate.getCertificateName());
            orderCertificate.setDescription(certificate.getCertificateDescription());
            orderCertificate.setPrice(certificate.getPrice());
            orderCertificate.setStartValidDate(Timestamp.valueOf(LocalDateTime.now()));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(orderCertificate.getStartValidDate());
            calendar.add(Calendar.DAY_OF_WEEK,certificate.getDuration());
            orderCertificate.setEndValidDate(new Timestamp(calendar.getTime().getTime()));
            List<Tag> tags = new ArrayList<>(certificate.getTags());
            orderCertificate.setTags(tags);
            orderCertificates.add(orderCertificate);
        });
        return orderCertificates;
    }
}

