package com.tungstun.barapi.presentation.controllers;

import com.tungstun.barapi.application.ProductService;
import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.presentation.dto.converter.ProductConverter;
import com.tungstun.barapi.presentation.dto.request.ProductRequest;
import com.tungstun.barapi.presentation.dto.response.ProductResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/api/bars/{barId}/products")
public class ProductController {
    private final ProductService productService;
    private final ProductConverter converter;

    public ProductController(ProductService productService, ProductConverter converter) {
        this.productService = productService;
        this.converter = converter;
    }

    @GetMapping
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Finds all products of bar",
            notes = "Provide id of bar to look up all products that are linked to the bar",
            response = ProductResponse.class
    )
    public ResponseEntity<List<ProductResponse>> getAllProductsOfBar(
            @ApiParam(value = "ID value for the bar you want to retrieve products from") @PathVariable("barId") Long barId,
            @ApiParam(value = "(Optional) String value to filter products on product type") @RequestParam(value = "productType", required = false) String productType,
            @ApiParam(value = "(Optional) Long value to filter products on category with id") @RequestParam(value = "categoryId", required = false) Long categoryId,
            @ApiParam(value = "(Optional) Boolean value to filter favorite products") @RequestParam(value = "onlyFavorites", required = false) Boolean onlyFavorites
    ) throws EntityNotFoundException {
        List<Product> products = this.productService.searchProductsOfBar(barId, productType, categoryId, onlyFavorites);
        return new ResponseEntity<>(converter.convertAll(products), HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Finds product of bar",
            notes = "Provide id of bar and product to look up the specific from the bar",
            response = ProductResponse.class
    )
    public ResponseEntity<ProductResponse> getProductOfBar(
            @ApiParam(value = "ID value for the bar you want to retrieve the product from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the product you want to retrieve") @PathVariable("productId") Long productId
    ) throws EntityNotFoundException {
        Product product = this.productService.getProductOfBar(barId, productId);
        return new ResponseEntity<>(converter.convert(product), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Creates new product for bar",
            notes = "Provide id of bar to add a new product with information from the request body to the bar",
            response = ProductResponse.class
    )
    public ResponseEntity<ProductResponse> addProductOfBar(
            @ApiParam(value = "ID value for the bar you want to create the product for") @PathVariable("barId") Long barId,
            @Valid @RequestBody ProductRequest productRequest
    ) throws EntityNotFoundException {
        Product product = this.productService.addProductToBar(barId, productRequest);
        return new ResponseEntity<>(converter.convert(product), HttpStatus.OK);
    }

    @PutMapping("/{productId}")
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Updates the product of bar",
            notes = "Provide id of bar and product to update the product with information from the request body",
            response = ProductResponse.class
    )
    public ResponseEntity<ProductResponse> updateProductOfBar(
            @ApiParam(value = "ID value for the bar you want to update the product from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the bar you want to update") @PathVariable("productId") Long productId,
            @Valid @RequestBody ProductRequest productRequest
    ) throws EntityNotFoundException {
        Product product = this.productService.updateProductOfBar(barId, productId, productRequest);
        return new ResponseEntity<>(converter.convert(product), HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Deletes the product of bar",
            notes = "Provide id of bar and product to delete the product from the bar",
            response = ProductResponse.class
    )
    public ResponseEntity<Void> deleteProductFromBar(
            @ApiParam(value = "ID value for the bar you want to delete the product from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the product you want to delete") @PathVariable("productId") Long productId
    ) throws EntityNotFoundException {
        this.productService.deleteProductOfBar(barId, productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/search")
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Searches products",
            notes = "Provide id of bar and search value to search for products with",
            response = ProductResponse.class
    )
    public ResponseEntity<List<ProductResponse>> searchProductInBar(
            @ApiParam(value = "ID value for the bar you want to search the product from") @PathVariable("barId") Long barId,
            @ApiParam(value = "The name to filter your search with") @Valid @RequestParam(value = "name") String name

    ) throws EntityNotFoundException {
        List<Product> foundProducts = this.productService.searchProduct(barId, name);
        return new ResponseEntity<>(converter.convertAll(foundProducts), HttpStatus.OK);
    }
}
