package com.epam.esm.service.impl;


import com.epam.esm.dto.mapper.impl.GiftCertificateMapper;
import com.epam.esm.dto.request.GiftCertificateDtoRequest;
import com.epam.esm.dto.response.GiftCertificateDtoResponse;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ApplicationException;
import com.epam.esm.repository.certificate.CertificateRepository;
import com.epam.esm.repository.tag.TagRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.service.util.QueryBuilder;
import com.epam.esm.util.Pagination;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.objenesis.instantiator.sun.MagicInstantiator;
import org.springframework.context.MessageSource;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GiftCertificateServiceImplTest {

    private final int DEFAULT_ID = 1;

    @Mock
    private CertificateRepository certificateRepository;

    @Mock
    private MessageSource messageSource;

    @Mock
    private TagService tagService;

    @Mock
    private GiftCertificateMapper mapper;

    @Spy
    private QueryBuilder builder =
            new QueryBuilder();

    @InjectMocks
    private GiftCertificateServiceImpl giftCertificateService;

    @Test
    void findGiftCertificateByIdTest() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(DEFAULT_ID);
        giftCertificate.setLastUpdateDate(Timestamp.valueOf("2022-10-10 13:02:11.0"));
        giftCertificate.setCreateDate(Timestamp.valueOf("2022-10-10 13:02:11.0"));

        Mockito.when(certificateRepository.findById(DEFAULT_ID)).thenReturn(Optional.of(giftCertificate));
        GiftCertificateDtoResponse response = giftCertificateService.getCertificateDtoById(DEFAULT_ID);

        assertEquals(mapper.toDTO(giftCertificate), response);
    }

    @Test
    void findGiftCertificateByIdShouldThrowException() {
        Mockito.when(certificateRepository.findById(DEFAULT_ID)).thenReturn(Optional.empty());

        assertThrows(ApplicationException.class, () -> giftCertificateService.getCertificateDtoById(DEFAULT_ID));
    }

    @Test
    void deleteGiftCertificateTest() {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setId(DEFAULT_ID);
        Mockito.when(certificateRepository.delete(DEFAULT_ID)).thenReturn(Boolean.TRUE);
        Mockito.when(certificateRepository.findById(DEFAULT_ID)).thenReturn(Optional.of(certificate));

        assertEquals(Boolean.TRUE, giftCertificateService.deleteCertificateById(DEFAULT_ID));
    }

    @Test
    void getFullCertificatesDataTest() {
        List<GiftCertificate> giftCertificateList = new ArrayList<>();
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setLastUpdateDate(Timestamp.valueOf("2022-10-10 13:02:11.0"));
        giftCertificate.setCreateDate(Timestamp.valueOf("2022-10-10 13:02:11.0"));
        giftCertificateList.add(giftCertificate);
        giftCertificateList.add(giftCertificate);

        Mockito.when(certificateRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(giftCertificate));
        List<GiftCertificate> resultGiftCertificateDtoList = giftCertificateService.getFullCertificatesData(giftCertificateList);

        assertEquals(2, resultGiftCertificateDtoList.size());
    }

    @Test
    void createNewCertificateTest() {
        GiftCertificateDtoRequest certificate = new GiftCertificateDtoRequest();
        certificate.setName("Tested");

        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setCertificateName("Tested");

        Mockito.when(mapper.toEntity(Mockito.any())).thenReturn(giftCertificate);
        Mockito.when(certificateRepository.findByName(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(certificateRepository.create(Mockito.any())).thenReturn(giftCertificate);
        Mockito.when(mapper.toDTO(Mockito.any())).thenReturn(new GiftCertificateDtoResponse());
        GiftCertificateDtoResponse response= giftCertificateService.createNewCertificate(certificate);

        assertNotNull(response);
    }

    @Test
    void createNewCertificateShouldThrowException() {
        GiftCertificateDtoRequest certificate = new GiftCertificateDtoRequest();
        certificate.setName("Tested");
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setCertificateName("Tested");
        Mockito.when(mapper.toEntity(Mockito.any())).thenReturn(giftCertificate);
        Mockito.when(certificateRepository.findByName(Mockito.any())).thenReturn(Optional.of(giftCertificate));

        assertThrows(ApplicationException.class, () -> giftCertificateService.createNewCertificate(certificate));
    }

    @Test
    void updateGiftCertificateTest() {
        GiftCertificateDtoRequest request = new GiftCertificateDtoRequest();
        request.setName("Test");
        request.setPrice(BigDecimal.ONE);
        request.setDescription("Desc");
        request.setDurationInDays(DEFAULT_ID);
        GiftCertificate certificate = new GiftCertificate();
        certificate.setCertificateName(request.getName());
        certificate.setTags(List.of(new Tag()));
        GiftCertificateDtoResponse response = new GiftCertificateDtoResponse();
        response.setName(request.getName());
        Mockito.when(mapper.toEntity(request)).thenReturn(certificate);
        Mockito.when(certificateRepository.findById(DEFAULT_ID)).thenReturn(Optional.of(certificate));
        Mockito.when(mapper.merge(Mockito.any(),Mockito.any())).thenReturn(certificate);
        Mockito.when(mapper.toDTO(certificate)).thenReturn(response);
        GiftCertificateDtoResponse resultGifCertificateDto = giftCertificateService.updateCertificate
                (DEFAULT_ID, request);

        assertEquals(request.getName(), resultGifCertificateDto.getName());
    }

    @Test
    void changePriceTest() {
        GiftCertificateDtoResponse certificateResponse = new GiftCertificateDtoResponse();
        certificateResponse.setPrice(BigDecimal.ONE);
        GiftCertificate certificate = new GiftCertificate();
        certificate.setPrice(BigDecimal.ONE);
        Mockito.when(certificateRepository.findById(Mockito.any())).thenReturn(Optional.of(certificate));
        Mockito.when(certificateRepository.update(Mockito.any())).thenReturn(certificate);
        Mockito.when(mapper.toDTO(Mockito.any())).thenReturn(certificateResponse);
        GiftCertificateDtoRequest request = new GiftCertificateDtoRequest();
        request.setPrice(BigDecimal.ONE);
        GiftCertificateDtoResponse response = giftCertificateService.changePrice(DEFAULT_ID, request);
        assertEquals(BigDecimal.ONE,response.getPrice());
    }

    @Test
    void changeDurationTest() {
        GiftCertificateDtoResponse certificateResponse = new GiftCertificateDtoResponse();
        certificateResponse.setDurationInDays(DEFAULT_ID);
        GiftCertificate certificate = new GiftCertificate();
        certificate.setDuration(DEFAULT_ID);
        Mockito.when(certificateRepository.findById(Mockito.any())).thenReturn(Optional.of(certificate));
        Mockito.when(certificateRepository.update(Mockito.any())).thenReturn(certificate);
        Mockito.when(mapper.toDTO(Mockito.any())).thenReturn(certificateResponse);
        GiftCertificateDtoRequest request = new GiftCertificateDtoRequest();
        request.setDurationInDays(DEFAULT_ID);
        GiftCertificateDtoResponse response = giftCertificateService.changeDuration(DEFAULT_ID,request);
        assertEquals(DEFAULT_ID,response.getDurationInDays());
    }

    @Test
    void changeDurationShouldThrowException() {
        assertThrows(ApplicationException.class, () -> {
            GiftCertificateDtoRequest request = new GiftCertificateDtoRequest();
            request.setDurationInDays(-1);
            giftCertificateService.changeDuration(DEFAULT_ID,request);
        });
    }

    @Test
    void changePriceShouldThrowException() {
        assertThrows(ApplicationException.class, () ->
        {
            GiftCertificateDtoRequest request = new GiftCertificateDtoRequest();
            request.setPrice(BigDecimal.valueOf(-1));
            giftCertificateService.changePrice(DEFAULT_ID,request);
        });
    }

    @Test
    void getCertificatesWithIdsTest(){
        List<Integer> list = List.of(1,2,3);
        List<GiftCertificate> certificates = giftCertificateService.getGiftCertificatesWithIds(list);
        assertEquals(list.size(),certificates.size());
        List<Integer> listFromCerf = new ArrayList<>();
        certificates.forEach(certificate -> listFromCerf.add(certificate.getId()));
        assertEquals(list,listFromCerf);
    }

    @Test
    void getCertificatesWithPaginationTest(){
        Pagination pagination = new Pagination(1,10);
        Mockito.when(certificateRepository.getAll(pagination,""))
                .thenReturn(List.of(new GiftCertificate()));
        Mockito.when(mapper.toDTO(Mockito.any())).thenReturn(new GiftCertificateDtoResponse());
        List<GiftCertificateDtoResponse> certificates= giftCertificateService.getCertificates(pagination,new HashMap<>());
        assertNotNull(certificates);
    }

    @Test
    void deleteGiftGiftCertificateTestShouldThrowException() {
        int id = 99;
        assertThrows(ApplicationException.class, () -> giftCertificateService.deleteCertificateById(id));
    }
}
