package com.epam.esm.dto.response;

import com.epam.esm.entity.OrderGiftCertificate;
import com.epam.esm.entity.User;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
public class OrderDtoResponse extends RepresentationModel<OrderDtoResponse> {

    private int id;

    private BigDecimal price;

    private String purchaseDate;

    private User user;

    private List<OrderGiftCertificateResponse> certificates;
}
