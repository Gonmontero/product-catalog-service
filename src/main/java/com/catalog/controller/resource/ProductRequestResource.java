package com.catalog.controller.resource;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequestResource {
    private String name;
    private BigDecimal price;
    private Integer quantity;
}
