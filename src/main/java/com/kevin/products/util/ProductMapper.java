package com.kevin.products.util;

import com.kevin.products.dto.ProductDto;
import com.kevin.products.model.Product;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

public class ProductMapper {

    public Product convertToDomain(ProductDto productDto) {
        ModelMapper modelMapper = new ModelMapper();
        Product product = modelMapper.map(productDto, Product.class);
        return product;
    }

    public ProductDto convertToDto(Product product) {
        ModelMapper modelMapper = new ModelMapper();
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        return productDto;
    }

    public List<ProductDto> convertToListDto(List<Product> listProduct) {
        List<ProductDto> listProductDto = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        for (Product product : listProduct) {
            ProductDto productDto = modelMapper.map(product, ProductDto.class);
            listProductDto.add(productDto);
        }
        return listProductDto;
    }
}
