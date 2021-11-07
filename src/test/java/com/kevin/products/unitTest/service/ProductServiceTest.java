package com.kevin.products.unitTest.service;

import com.kevin.products.dto.ProductDto;
import com.kevin.products.exception.ProductBadRequestException;
import com.kevin.products.exception.ProductExceptionEnum;
import com.kevin.products.exception.ProductNotFoundException;
import com.kevin.products.model.Product;
import com.kevin.products.repository.ProductRepository;
import com.kevin.products.service.ProductService;
import com.kevin.products.service.impl.ProductServiceImpl;
import com.kevin.products.util.ProductMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    private ProductService underTest;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    private final Product PRODUCT_TEST = new Product("clock", 10.0, 50);

    private final ProductDto PRODUCT_DTO_TEST = new ProductDto("clock", 10.0, 50);

    private final ProductDto PRODUCT_DTO_BUY_TEST = new ProductDto("clock", 10.0, 10);

    private final ProductDto PRODUCT_DTO_UPDATE_TEST = new ProductDto("clock", 10.0, 40);

    private final String PRODUCT_NOT_FOUND =
            ProductExceptionEnum.PRODUCT_NOT_FOUND.getCode() + " - "
                    + ProductExceptionEnum.PRODUCT_NOT_FOUND.getMessage();

    private final String PRODUCT_NAME_EXIST =
            ProductExceptionEnum.PRODUCT_NAME_EXIST.getCode() + " - "
                    + ProductExceptionEnum.PRODUCT_NAME_EXIST.getMessage();

    private final String NOT_ENOUGH_STOCK =
            ProductExceptionEnum.NOT_ENOUGH_STOCK.getCode() + " - "
                    + ProductExceptionEnum.NOT_ENOUGH_STOCK.getMessage();

    @BeforeEach
    public void setup() {
        underTest = new ProductServiceImpl(productRepository, productMapper);
    }

    @Test
    public void getAllProductsTest() {

        when(productMapper.convertToListDto(List.of(PRODUCT_TEST))).thenReturn(List.of(PRODUCT_DTO_TEST));

        when(productRepository.findAll()).thenReturn(List.of(PRODUCT_TEST));

        Assertions.assertTrue(underTest.getAllProducts().size() > 0);
    }

    @Test
    public void getProductByIdTest() {

        when(productRepository.findById(any())).thenReturn(java.util.Optional.of(PRODUCT_TEST));
        when(productMapper.convertToDto(PRODUCT_TEST)).thenReturn(PRODUCT_DTO_TEST);
        ProductDto test = underTest.getProductById(PRODUCT_TEST.getProductId());

        Assertions.assertEquals(test.getName(), PRODUCT_TEST.getName());

    }

    @Test
    public void getProductByIdExceptionProductNotFoundTest() {

        when(productRepository.findById(any())).thenThrow(new ProductNotFoundException(ProductExceptionEnum.PRODUCT_NOT_FOUND));

        Exception exception = assertThrows(ProductNotFoundException.class, () -> {
            underTest.getProductById(-1);
        });

        Assertions.assertEquals(exception.getMessage(), PRODUCT_NOT_FOUND);
    }

    @Test
    public void createProductTest() throws Exception {

        when(productRepository.save(PRODUCT_TEST)).thenReturn(PRODUCT_TEST);
        when(productMapper.convertToDomain(PRODUCT_DTO_TEST)).thenReturn(PRODUCT_TEST);
        when(productMapper.convertToDto(PRODUCT_TEST)).thenReturn(PRODUCT_DTO_TEST);

        Assertions.assertEquals(underTest.createProduct(PRODUCT_DTO_TEST).getName(), PRODUCT_DTO_TEST.getName());
    }

    @Test
    public void createProductExceptionProductNameDuplicateTest() {

        when(productRepository.findByName(any())).thenReturn(PRODUCT_TEST);
        when(productMapper.convertToDomain(PRODUCT_DTO_TEST)).thenReturn(PRODUCT_TEST);

        Exception exception = assertThrows(ProductBadRequestException.class, () -> {
            underTest.createProduct(PRODUCT_DTO_TEST);
        });

        Assertions.assertEquals(exception.getMessage(), PRODUCT_NAME_EXIST);
    }

    @Test
    public void deleteProductTest() {

        when(productRepository.findById(any())).thenReturn(java.util.Optional.of(PRODUCT_TEST));
        Mockito.doNothing().when(productRepository).deleteById(any());
        Assertions.assertEquals(underTest.deleteProduct(5), true);
    }

    @Test
    public void deleteProductExceptionProductNotFoundTest() {

        when(productRepository.findById(any())).thenThrow(new ProductNotFoundException(ProductExceptionEnum.PRODUCT_NOT_FOUND));

        Exception exception = assertThrows(ProductNotFoundException.class, () -> {
            underTest.deleteProduct(-1);
        });

        Assertions.assertEquals(exception.getMessage(), PRODUCT_NOT_FOUND);
    }

    @Test
    public void buyProductTest() {

        when(productRepository.findById(any())).thenReturn(java.util.Optional.of(PRODUCT_TEST));
        when(productRepository.save(PRODUCT_TEST)).thenReturn(PRODUCT_TEST);
        when(productMapper.convertToDto(PRODUCT_TEST)).thenReturn(PRODUCT_DTO_BUY_TEST);

        ProductDto productDto = underTest.buyProduct(PRODUCT_DTO_TEST.getProductId(), 40);

        Assertions.assertEquals(productDto.getStock(), 10);
    }

    @Test
    public void buyProductExceptionProductNotFoundTest() {

        when(productRepository.findById(any())).thenThrow(new ProductNotFoundException(ProductExceptionEnum.PRODUCT_NOT_FOUND));

        Exception exception = assertThrows(ProductNotFoundException.class, () -> {
            underTest.buyProduct(-1, 50);
        });

        Assertions.assertEquals(exception.getMessage(), PRODUCT_NOT_FOUND);
    }

    @Test
    public void buyProductExceptionProductNotEnoughStockTest() {

        when(productRepository.findById(any())).thenReturn(java.util.Optional.of(PRODUCT_TEST));

        Exception exception = assertThrows(ProductBadRequestException.class, () -> {
            underTest.buyProduct(PRODUCT_TEST.getProductId(), 51);
        });

        Assertions.assertEquals(exception.getMessage(), NOT_ENOUGH_STOCK);
    }

    @Test
    public void updateStockTest() {

        when(productRepository.findById(any())).thenReturn(java.util.Optional.of(PRODUCT_TEST));
        when(productRepository.save(PRODUCT_TEST)).thenReturn(PRODUCT_TEST);
        when(productMapper.convertToDto(PRODUCT_TEST)).thenReturn(PRODUCT_DTO_UPDATE_TEST);

        ProductDto productDto = underTest.updateStock(PRODUCT_TEST.getProductId(), 40);

        Assertions.assertEquals(productDto.getStock(), 40);
    }

    @Test
    public void updateStocktExceptionProductNotFoundTest() {

        when(productRepository.findById(any())).thenThrow(new ProductNotFoundException(ProductExceptionEnum.PRODUCT_NOT_FOUND));

        Exception exception = assertThrows(ProductNotFoundException.class, () -> {
            underTest.updateStock(-1, 50);
        });

        Assertions.assertEquals(exception.getMessage(), PRODUCT_NOT_FOUND);
    }
}
