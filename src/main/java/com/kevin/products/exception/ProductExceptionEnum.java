package com.kevin.products.exception;

public enum ProductExceptionEnum {
    PRODUCT_NOT_FOUND("001", "Product not found"),
    PRODUCT_NAME_EXIST("002", "Product name already exist"),
    NOT_ENOUGH_STOCK("003", "There is not enough stock");

    private String code;
    private String message;

    ProductExceptionEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
