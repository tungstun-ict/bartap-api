package com.tungstun.barapi.presentation.controllers;

import com.tungstun.barapi.application.ProductService;
import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.presentation.dto.request.ProductRequest;
import com.tungstun.barapi.presentation.dto.response.ProductResponse;
import com.tungstun.barapi.presentation.mapper.ResponseMapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.naming.directory.InvalidAttributesException;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/bars/{barId}/products")
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
    @PreAuthorize("hasPermission(#barId, 'ROLE_BAR_OWNER')")
    @ApiOperation(
            value = "Finds all products of bar",
            notes = "Provide id of bar to look up all products that are linked to the bar",
            response = ProductResponse.class,
            responseContainer = "List"
    )
    public ResponseEntity<List<ProductResponse>> getAllProductsOfBar(
            @ApiParam(value = "ID value for the bar you want to retrieve products from") @PathVariable("barId") Long barId,
            @ApiParam(value = "(Optional) String value to filter products on product type") @RequestParam(value = "productType", required = false) String productType,
            @ApiParam(value = "(Optional) Long value to filter products on category with id") @RequestParam(value = "categoryId", required = false) Long categoryId,
            @ApiParam(value = "(Optional) Boolean value to filter favorite products") @RequestParam(value = "onlyFavorites", required = false) Boolean onlyFavorites
    ) throws NotFoundException {
        List<Product> products = this.PRODUCT_SERVICE.searchProductsOfBar(barId, productType, categoryId, onlyFavorites);
        List<ProductResponse> productResponse = RESPONSE_MAPPER.convertList(products, ProductResponse.class);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    @PreAuthorize("hasPermission(#barId, 'ROLE_BAR_OWNER')")
    @ApiOperation(
            value = "Finds product of bar",
            notes = "Provide id of bar and product to look up the specific from the bar",
            response = ProductResponse.class
    )
    public ResponseEntity<ProductResponse> getProductOfBar(
            @ApiParam(value = "ID value for the bar you want to retrieve the product from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the product you want to retrieve") @PathVariable("productId") Long productId
    ) throws NotFoundException {
        Product product = this.PRODUCT_SERVICE.getProductOfBar(barId, productId);
        return new ResponseEntity<>(convertToProductResult(product), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasPermission(#barId, 'ROLE_BAR_OWNER')")
    @ApiOperation(
            value = "Creates new product for bar",
            notes = "Provide id of bar to add a new product with information from the request body to the bar",
            response = ProductResponse.class
    )
    public ResponseEntity<ProductResponse> addProductOfBar(
            @ApiParam(value = "ID value for the bar you want to create the product for") @PathVariable("barId") Long barId,
            @Valid @RequestBody ProductRequest productRequest
    ) throws NotFoundException, InvalidAttributesException {
        Product product = this.PRODUCT_SERVICE.addProductToBar(barId, productRequest);
        return new ResponseEntity<>(convertToProductResult(product), HttpStatus.OK);
    }

    @PutMapping("/{productId}")
    @PreAuthorize("hasPermission(#barId, 'ROLE_BAR_OWNER')")
    @ApiOperation(
            value = "Updates the product of bar",
            notes = "Provide id of bar and product to update the product with information from the request body",
            response = ProductResponse.class
    )
    public ResponseEntity<ProductResponse> updateProductOfBar(
            @ApiParam(value = "ID value for the bar you want to update the product from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the bar you want to update") @PathVariable("productId") Long productId,
            @Valid @RequestBody ProductRequest productRequest
    ) throws NotFoundException, InvalidAttributesException {
        Product product = this.PRODUCT_SERVICE.updateProductOfBar(barId, productId, productRequest);
        return new ResponseEntity<>(convertToProductResult(product), HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasPermission(#barId, 'ROLE_BAR_OWNER')")
    @ApiOperation(
            value = "Deletes the product of bar",
            notes = "Provide id of bar and product to delete the product from the bar",
            response = ProductResponse.class
    )
    public ResponseEntity<Void> deleteProductFromBar(
            @ApiParam(value = "ID value for the bar you want to delete the product from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the product you want to delete") @PathVariable("productId") Long productId
    ) throws NotFoundException {
        this.PRODUCT_SERVICE.deleteProductOfBar(barId, productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/search")
    @PreAuthorize("hasPermission(#barId, 'ROLE_BAR_OWNER')")
    @ApiOperation(
            value = "Searches products",
            notes = "Provide id of bar and search value to search for products with",
            response = ProductResponse.class
    )
    public ResponseEntity<List<ProductResponse>> deleteProductFromBar(
            @ApiParam(value = "ID value for the bar you want to search the product from") @PathVariable("barId") Long barId,
            @ApiParam(value = "The name to filter your search with") @Valid @RequestParam(value = "name") String name

    ) throws NotFoundException {
        List<Product> foundProducts = this.PRODUCT_SERVICE.searchProduct(barId, name);
        List<ProductResponse> foundProductResponses = foundProducts.stream()
                .map(this::convertToProductResult)
                .collect(Collectors.toList());
        return new ResponseEntity<>(foundProductResponses, HttpStatus.OK);
    }
}
