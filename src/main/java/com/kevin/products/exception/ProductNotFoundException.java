package com.kevin.products.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(ProductExceptionEnum exception) {
        super(exception.getCode() + " - " + exception.getMessage());
    }

}
