package com.kevin.products.controller;

import com.kevin.products.dto.ProductDto;
import com.kevin.products.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping(value = "/getAllProducts")
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping(value = "/getProduct")
    public ProductDto getProduct(@RequestParam("id") Integer id) {
        return productService.getProductById(id);
    }

    @PostMapping(value = "/postProduct")
    public ProductDto postProduct(@RequestBody ProductDto productDto) {
        return productService.createProduct(productDto);
    }

    @DeleteMapping(value = "/deleteProduct")
    public Boolean deleteProduct(@RequestParam("id") Integer id) {
        return productService.deleteProduct(id);
    }

    @PutMapping(value = "/buyProduct")
    public ProductDto buyProduct(@RequestParam("id") Integer id, @RequestParam("amount") Integer amount) {
        return productService.buyProduct(id, amount);
    }

    @PutMapping(value = "/updateStock")
    public ProductDto updateStock(@RequestParam("id") Integer id, @RequestParam("amount") Integer amount) {
        return productService.updateStock(id, amount);
    }
}
