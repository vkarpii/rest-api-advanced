package com.epam.esm.dto.response;

import com.epam.esm.entity.Tag;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.util.List;

@Data
public class GiftCertificateDtoResponse extends RepresentationModel<GiftCertificateDtoResponse> {
    private int id;
    private String name;
    private String description;
    private BigDecimal price;
    private List<Tag> tags;
    private String createDate;
    private String lastUpdateDate;
    private int durationInDays;
}
