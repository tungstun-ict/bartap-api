package com.tungstun.barapi.presentation.controllers;

import com.tungstun.barapi.application.ProductService;
import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.presentation.dto.request.ProductRequest;
import com.tungstun.barapi.presentation.dto.response.ProductResponse;
import com.tungstun.barapi.presentation.mapper.ResponseMapper;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.naming.directory.InvalidAttributesException;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("bars/{barId}/products")
@RolesAllowed("ROLE_BAR_OWNER")
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
            @PathVariable("barId") Long barId,
            @RequestParam(value = "productType", required = false) String productType,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "onlyFavorites", required = false) Boolean onlyFavorites
    ) throws NotFoundException {
        List<Product> products = this.PRODUCT_SERVICE.getProductsOfBar(barId, productType, categoryId, onlyFavorites);
        List<ProductResponse> productResponse = RESPONSE_MAPPER.convertList(products, ProductResponse.class);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductOfBar(
            @PathVariable("barId") Long barId,
            @PathVariable("productId") Long productId
    ) throws NotFoundException {
        Product product = this.PRODUCT_SERVICE.getProductOfBar(barId, productId);
        return new ResponseEntity<>(convertToProductResult(product), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> addProductOfBar(
            @PathVariable("barId") Long barId,
            @Valid @RequestBody ProductRequest productRequest
    ) throws NotFoundException, InvalidAttributesException {
        Product product = this.PRODUCT_SERVICE.addProductToBar(barId, productRequest);
        return new ResponseEntity<>(convertToProductResult(product), HttpStatus.OK);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProductOfBar(
            @PathVariable("barId") Long barId,
            @PathVariable("productId") Long productId,
            @Valid @RequestBody ProductRequest productRequest
    ) throws NotFoundException, InvalidAttributesException {
        Product product = this.PRODUCT_SERVICE.updateProductOfBar(barId, productId, productRequest);
        return new ResponseEntity<>(convertToProductResult(product), HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProductFromBar(
            @PathVariable("barId") Long barId,
            @PathVariable("productId") Long productId
    ) throws NotFoundException {
        this.PRODUCT_SERVICE.deleteProductOfBar(barId, productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
