package com.kevin.products.service;

import com.kevin.products.dto.ProductDto;

import java.util.List;

public interface ProductService {

    List<ProductDto> getAllProducts();

    ProductDto getProductById(Integer id);

    ProductDto createProduct(ProductDto productDto);

    Boolean deleteProduct(Integer id);

    ProductDto buyProduct(Integer id, Integer amount);

    ProductDto updateStock(Integer id, Integer amount);
}
