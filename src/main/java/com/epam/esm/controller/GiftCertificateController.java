package com.epam.esm.controller;

import com.epam.esm.assembler.GiftCertificateAssembler;
import com.epam.esm.dto.request.GiftCertificateDtoRequest;
import com.epam.esm.dto.response.GiftCertificateDtoResponse;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.util.Pagination;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/gift-certificate")
public class GiftCertificateController {
    private final GiftCertificateService service;
    private final GiftCertificateAssembler assembler;

    @Autowired
    public GiftCertificateController(GiftCertificateService service, GiftCertificateAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @GetMapping("/{id}")
    public GiftCertificateDtoResponse find(@PathVariable int id) {
        return assembler.toModel(service.getCertificateDtoById(id));
    }

    @GetMapping
    public List<GiftCertificateDtoResponse> findAll(Pagination pagination, @RequestParam Map<String, String> params) {
        return assembler.toCollectionModel(service.getCertificates(pagination,params))
                .getContent()
                .stream()
                .toList();
    }

    @PostMapping
    public GiftCertificateDtoResponse create(@RequestBody @Valid GiftCertificateDtoRequest certificateDto) {
        return assembler.toModel(service.createNewCertificate(certificateDto));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        service.deleteCertificateById(id);
    }

    @PutMapping("/{id}")
    public GiftCertificateDtoResponse update(@PathVariable int id,
                                             @RequestBody GiftCertificateDtoRequest certificateDto) {
        return assembler.toModel(service.updateCertificate(id, certificateDto));
    }

    @PutMapping("/price/{id}")
    public GiftCertificateDtoResponse changePrice(@PathVariable int id,
                                                  @RequestBody GiftCertificateDtoRequest certificateDto) {
        return assembler.toModel(service.changePrice(id, certificateDto));
    }

    @PutMapping("/duration/{id}")
    public GiftCertificateDtoResponse changeDuration(@PathVariable int id,
                                                     @RequestBody GiftCertificateDtoRequest certificateDto) {
        return assembler.toModel(service.changeDuration(id, certificateDto));
    }
}
