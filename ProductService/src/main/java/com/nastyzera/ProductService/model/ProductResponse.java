package com.nastyzera.ProductService.model;

import lombok.Data;

@Data
public class ProductResponse {
    private String productName;
    private long productId;
    private long quantity;
    private long price;    
}
