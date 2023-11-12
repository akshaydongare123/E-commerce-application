package com.nastyzera.ProductService.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nastyzera.ProductService.entity.Product;
import com.nastyzera.ProductService.exception.ProductServiceCustomException;
import com.nastyzera.ProductService.model.ProductRequest;
import com.nastyzera.ProductService.model.ProductResponse;
import com.nastyzera.ProductService.repository.ProductRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public long addProduct(ProductRequest productRequest) {
        log.info("Adding Product..");
        Product product = Product.builder()
                .productName(productRequest.getName())
                .quantity(productRequest.getQuantity())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product);

        log.info("Product Created");
        return product.getProductId();
    }

    @Override
    public void reduceQuantity(long productId, long quantity) {
        log.info("Product id {} stock reduction"+productId);
        Product product
        = productRepository.findById(productId)
        .orElseThrow(() -> new ProductServiceCustomException(
                "Product with given Id not found",
                "PRODUCT_NOT_FOUND"
        ));
        if(product.getQuantity() < quantity){
            throw new ProductServiceCustomException("Product "+product.getProductName()+" is out of stock", "PRODUCT_OOS");
        }else{
        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
        log.info("Product quantity updated successfully");
        }
    }

    @Override
    public ProductResponse getProductById(long productId) {
        log.info("Get the product for productId: {}", productId);
        Product product
        = productRepository.findById(productId)
        .orElseThrow(() -> new ProductServiceCustomException(
                "Product with given Id not found",
                "PRODUCT_NOT_FOUND"
        ));

        ProductResponse productResponse
                = new ProductResponse();

        BeanUtils.copyProperties(product, productResponse);


        return productResponse;
    }

}
