package com.nastyzera.ProductService.service;

import com.nastyzera.ProductService.model.ProductRequest;
import com.nastyzera.ProductService.model.ProductResponse;

public interface ProductService {

    long addProduct(ProductRequest productRequest);

    void reduceQuantity(long productId, long quantity);

    ProductResponse getProductById(long productId);
    
}
