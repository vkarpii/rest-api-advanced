package com.epam.esm.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class OrderDetailsDtoResponse {
    private BigDecimal price;

    private String purchaseDate;

}
