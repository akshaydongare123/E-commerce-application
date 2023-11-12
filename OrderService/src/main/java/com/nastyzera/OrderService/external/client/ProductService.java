package com.nastyzera.OrderService.external.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nastyzera.OrderService.exception.CustomException;
import com.nastyzera.OrderService.external.response.ProductResponse;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@CircuitBreaker(name = "external", fallbackMethod = "fallback")
@FeignClient(name = "PRODUCT-SERVICE/product")
public interface ProductService {
        
        @PutMapping("/reduceQuantity/{id}")
        public ResponseEntity<Void> reduceQuantity(
                        @PathVariable("id") long productId,
                        @RequestParam long quantity);

        @GetMapping("/{id}")
        public ResponseEntity<ProductResponse> getProductById(@PathVariable("id") long productId);

        default ResponseEntity<Long> fallback(Exception e) {
                throw new CustomException("Product Service is not available",
                                "UNAVAILABLE",
                                500);
        }
}
