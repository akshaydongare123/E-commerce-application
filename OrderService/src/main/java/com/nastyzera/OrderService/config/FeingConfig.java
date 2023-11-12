package com.nastyzera.OrderService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nastyzera.OrderService.external.decoder.CustomErrorDecoder;

import feign.codec.ErrorDecoder;

@Configuration
public class FeingConfig {
    @Bean
    ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }
}
