package com.kevin.products.service.impl;

import com.kevin.products.dto.ProductDto;
import com.kevin.products.exception.ProductBadRequestException;
import com.kevin.products.exception.ProductExceptionEnum;
import com.kevin.products.exception.ProductNotFoundException;
import com.kevin.products.model.Product;
import com.kevin.products.repository.ProductRepository;
import com.kevin.products.service.ProductService;
import com.kevin.products.util.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public List<ProductDto> getAllProducts() {
        return productMapper.convertToListDto(productRepository.findAll());
    }

    @Override
    public ProductDto getProductById(Integer id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(ProductExceptionEnum.PRODUCT_NOT_FOUND));
        ProductDto productDto = productMapper.convertToDto(product);
        return productDto;
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = productMapper.convertToDomain(productDto);
        if (alreadyExistsProductName(product))
            throw new ProductBadRequestException(ProductExceptionEnum.PRODUCT_NAME_EXIST);
        ProductDto productDtoSaved = productMapper.convertToDto(productRepository.save(product));
        return productDtoSaved;
    }

    @Override
    public Boolean deleteProduct(Integer id) {
        productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(ProductExceptionEnum.PRODUCT_NOT_FOUND));
        productRepository.deleteById(id);
        return true;
    }

    @Override
    public ProductDto buyProduct(Integer id, Integer amount) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(ProductExceptionEnum.PRODUCT_NOT_FOUND));
        product.setStock(substract(product.getStock(), amount));
        productRepository.save(product);
        ProductDto productDto = productMapper.convertToDto(product);
        return productDto;
    }

    @Override
    public ProductDto updateStock(Integer id, Integer amount) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(ProductExceptionEnum.PRODUCT_NOT_FOUND));
        product.setStock(amount);
        ProductDto productDto = productMapper.convertToDto(productRepository.save(product));
        return productDto;
    }

    private boolean alreadyExistsProductName(Product product) {
        Optional<Product> productChecked = Optional.ofNullable(productRepository.findByName(product.getName()));
        return productChecked.isPresent();
    }

    private Integer substract(Integer stock, Integer amount) {
        if (amount > stock) throw new ProductBadRequestException(ProductExceptionEnum.NOT_ENOUGH_STOCK);
        return stock - amount;
    }

}
