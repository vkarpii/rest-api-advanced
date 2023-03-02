package com.epam.esm.dto.response;

import com.epam.esm.entity.Tag;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
public class OrderGiftCertificateResponse {
    private int id;

    private String name;

    private String description;

    private BigDecimal price;

    private String startValidDate;

    private String endValidDate;

    private List<Tag> tags;
}
