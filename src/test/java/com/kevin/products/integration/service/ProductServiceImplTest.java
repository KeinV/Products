package com.kevin.products.integration.service;

import com.kevin.products.exception.ProductBadRequestException;
import com.kevin.products.exception.ProductExceptionEnum;
import com.kevin.products.exception.ProductNotFoundException;
import com.kevin.products.model.Product;
import com.kevin.products.repository.ProductRepository;
import com.kevin.products.service.ProductService;
import com.kevin.products.service.impl.ProductServiceImpl;
import com.kevin.products.dto.ProductDto;
import com.kevin.products.util.ProductMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ProductServiceImplTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    private ProductService underTest;

    private final Product PRODUCT_TEST = new Product("clock", 10.0, 50);

    private final ProductDto PRODUCT_DTO_TEST = new ProductDto("clock", 10.0, 50);

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
    @Transactional
    public void getAllProductsTest() {

        productRepository.save(PRODUCT_TEST);

        List<ProductDto> productList = underTest.getAllProducts();

        Assertions.assertTrue(productList.size() > 0);
    }

    @Test
    @Transactional
    public void getProductByIdTest() {

        Product productSaved = productRepository.save(PRODUCT_TEST);

        ProductDto productDto = underTest.getProductById(productSaved.getProductId());

        Assertions.assertEquals(productDto.getProductId(), productSaved.getProductId());
    }

    @Test
    public void getProductByIdExceptionProductNotFoundTest() {

        Exception exception = assertThrows(ProductNotFoundException.class, () -> {
            underTest.getProductById(-1);
        });

        Assertions.assertEquals(exception.getMessage(), PRODUCT_NOT_FOUND);
    }

    @Test
    @Transactional
    public void createProductTest() {

        ProductDto productDto = underTest.createProduct(PRODUCT_DTO_TEST);

        Assertions.assertEquals(productDto.getName(), PRODUCT_DTO_TEST.getName());
    }

    @Test
    @Transactional
    public void createProductExceptionProductNameDuplicateTest() {

        productRepository.save(PRODUCT_TEST);

        Exception exception = assertThrows(ProductBadRequestException.class, () -> {
            underTest.createProduct(PRODUCT_DTO_TEST);
        });

        Assertions.assertEquals(exception.getMessage(), PRODUCT_NAME_EXIST);
    }

    @Test
    @Transactional
    public void deleteProductTest() {

        Product productSaved = productRepository.save(PRODUCT_TEST);

        Boolean result = underTest.deleteProduct(productSaved.getProductId());

        Assertions.assertTrue(result);
    }

    @Test
    public void deleteProductExceptionProductNotFoundTest() {

        Exception exception = assertThrows(ProductNotFoundException.class, () -> {
            underTest.deleteProduct(-1);
        });

        Assertions.assertEquals(exception.getMessage(), PRODUCT_NOT_FOUND);
    }

    @Test
    @Transactional
    public void buyProductTest() {

        Product productSaved = productRepository.save(PRODUCT_TEST);

        ProductDto productDto = underTest.buyProduct(productSaved.getProductId(), 40);

        Assertions.assertEquals(productDto.getStock(), 10);
    }

    @Test
    public void buyProductExceptionProductNotFoundTest() {

        Exception exception = assertThrows(ProductNotFoundException.class, () -> {
            underTest.buyProduct(-1, 50);
        });

        Assertions.assertEquals(exception.getMessage(), PRODUCT_NOT_FOUND);
    }

    @Test
    @Transactional
    public void buyProductExceptionProductNotEnoughStockTest() {

        Product productSaved = productRepository.save(PRODUCT_TEST);

        Exception exception = assertThrows(ProductBadRequestException.class, () -> {
            underTest.buyProduct(productSaved.getProductId(), 51);
        });

        Assertions.assertEquals(exception.getMessage(), NOT_ENOUGH_STOCK);
    }

    @Test
    @Transactional
    public void updateStockTest() {

        Product productSaved = productRepository.save(PRODUCT_TEST);

        ProductDto productDto = underTest.updateStock(productSaved.getProductId(), 40);

        Assertions.assertEquals(productDto.getStock(), 40);
    }

    @Test
    public void updateStocktExceptionProductNotFoundTest() {

        Exception exception = assertThrows(ProductNotFoundException.class, () -> {
            underTest.updateStock(-1, 50);
        });

        Assertions.assertEquals(exception.getMessage(), PRODUCT_NOT_FOUND);
    }
}
