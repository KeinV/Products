package com.kevin.products.exception;

public class ProductBadRequestException extends RuntimeException {

    public ProductBadRequestException(ProductExceptionEnum exception) {
        super(exception.getCode() + " - " + exception.getMessage());
    }
}
