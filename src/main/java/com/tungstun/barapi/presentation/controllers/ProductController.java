package com.tungstun.barapi.presentation.controllers;

import com.tungstun.barapi.application.ProductService;
import com.tungstun.barapi.domain.Product;
import com.tungstun.barapi.presentation.dto.response.ProductResponse;
import com.tungstun.barapi.presentation.mapper.ResponseMapper;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("bars/{barId}/products")
public class ProductController {
    private final ProductService PRODUCT_SERVICE;
    private final ResponseMapper RESPONSE_MAPPER;

    public ProductController(ProductService productService, ResponseMapper responseMapper) {
        this.PRODUCT_SERVICE = productService;
        this.RESPONSE_MAPPER = responseMapper;
    }

    private ProductResponse convertToProductResult(Product product){
        return RESPONSE_MAPPER.convert(product, ProductResponse.class);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProductsOfBar(
            @PathVariable("barId") Long barId
    ) throws NotFoundException {
        List<Product> products = this.PRODUCT_SERVICE.getAllProductsOfBar(barId);
        List<ProductResponse> productResponse = RESPONSE_MAPPER.convertList(products, ProductResponse.class);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }
}
