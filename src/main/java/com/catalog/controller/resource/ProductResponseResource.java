package com.catalog.controller.resource;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponseResource {
    private String id;
    private String name;
    private BigDecimal price;
    private Integer quantity;
}
