package com.kevin.products.configuration;

import com.kevin.products.repository.ProductRepository;
import com.kevin.products.service.ProductService;
import com.kevin.products.service.impl.ProductServiceImpl;
import com.kevin.products.util.ProductMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductConfiguration {

    @Bean
    public ProductMapper productMapper() {
        return new ProductMapper();
    }

    @Bean
    public ProductService productService(ProductRepository productRepository, ProductMapper productMapper) {
        return new ProductServiceImpl(productRepository, productMapper);
    }
}
