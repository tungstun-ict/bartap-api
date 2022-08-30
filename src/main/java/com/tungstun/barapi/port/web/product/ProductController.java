package com.tungstun.barapi.port.web.product;

import com.tungstun.barapi.application.product.ProductCommandHandler;
import com.tungstun.barapi.application.product.ProductQueryHandler;
import com.tungstun.barapi.application.product.command.CreateProduct;
import com.tungstun.barapi.application.product.command.DeleteProduct;
import com.tungstun.barapi.application.product.command.UpdateProduct;
import com.tungstun.barapi.application.product.query.GetProduct;
import com.tungstun.barapi.application.product.query.ListProductsOfBar;
import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.port.web.product.converter.ProductConverter;
import com.tungstun.barapi.port.web.product.request.CreateProductRequest;
import com.tungstun.barapi.port.web.product.request.UpdateProductRequest;
import com.tungstun.barapi.port.web.product.response.ProductResponse;
import com.tungstun.common.response.UuidResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/bars/{barId}/products")
public class ProductController {
    private final ProductCommandHandler productCommandHandler;
    private final ProductQueryHandler productQueryHandler;
    private final ProductConverter converter;

    public ProductController(ProductCommandHandler productCommandHandler, ProductQueryHandler productQueryHandler, ProductConverter converter) {
        this.productCommandHandler = productCommandHandler;
        this.productQueryHandler = productQueryHandler;
        this.converter = converter;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Finds all products of bar",
            notes = "Provide categoryId of bar to look up all products that are linked to the bar",
            response = ProductResponse.class
    )
    public List<ProductResponse> getAllProductsOfBar(
            @ApiParam(value = "ID value for the bar you want to retrieve products from") @PathVariable("barId") UUID barId,
            @ApiParam(value = "(Optional) String value to filter products on product type") @RequestParam(value = "productType", required = false) String productType,
            @ApiParam(value = "(Optional) UUID value to filter products on category with categoryId") @RequestParam(value = "categoryId", required = false) UUID categoryId,
            @ApiParam(value = "(Optional) Boolean value to filter favorite products") @RequestParam(value = "onlyFavorites", required = false) Boolean onlyFavorites,
            @ApiParam(value = "(Optional) The name to filter your search with") @Valid @RequestParam(value = "searchText", required = false) String searchText
    ) throws EntityNotFoundException {
        List<Product> products = productQueryHandler.handle(new ListProductsOfBar(
                barId,
                categoryId,
                onlyFavorites,
                productType,
                searchText
        ));
        return converter.convertAll(products);
    }

    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Finds product of bar",
            notes = "Provide categoryId of bar and product to look up the specific from the bar",
            response = ProductResponse.class
    )
    public ProductResponse getProductOfBar(
            @ApiParam(value = "ID value for the bar you want to retrieve the product from") @PathVariable("barId") UUID barId,
            @ApiParam(value = "ID value for the product you want to retrieve") @PathVariable("productId") UUID productId
    ) throws EntityNotFoundException {
        Product product = productQueryHandler.handle(new GetProduct(barId, productId));
        return converter.convert(product);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Creates new product for bar",
            notes = "Provide categoryId of bar to add a new product with information from the request body to the bar",
            response = ProductResponse.class
    )
    public UuidResponse addProductOfBar(
            @ApiParam(value = "ID value for the bar you want to create the product for") @PathVariable("barId") UUID barId,
            @Valid @RequestBody CreateProductRequest request
    ) throws EntityNotFoundException {
        CreateProduct command = new CreateProduct(
                barId,
                request.name(),
                request.brand(),
                request.size(),
                request.price(),
                request.isFavorite(),
                request.productType(),
                request.categoryId()
        );
        return new UuidResponse(productCommandHandler.handle(command));
    }

    @PutMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Updates the product of bar",
            notes = "Provide categoryId of bar and product to update the product with information from the request body",
            response = ProductResponse.class
    )
    public UuidResponse updateProductOfBar(
            @ApiParam(value = "ID value for the bar you want to update the product from") @PathVariable("barId") UUID barId,
            @ApiParam(value = "ID value for the bar you want to update") @PathVariable("productId") UUID productId,
            @Valid @RequestBody UpdateProductRequest request
    ) throws EntityNotFoundException {
        UpdateProduct command = new UpdateProduct(
                barId,
                productId,
                request.name(),
                request.brand(),
                request.size(),
                request.price(),
                request.isFavorite(),
                request.productType(),
                request.categoryId()
        );
        return new UuidResponse(productCommandHandler.handle(command));
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Deletes the product of bar",
            notes = "Provide categoryId of bar and product to delete the product from the bar",
            response = ProductResponse.class
    )
    public void deleteProductFromBar(
            @ApiParam(value = "ID value for the bar you want to delete the product from") @PathVariable("barId") UUID barId,
            @ApiParam(value = "ID value for the product you want to delete") @PathVariable("productId") UUID productId
    ) throws EntityNotFoundException {
        DeleteProduct command = new DeleteProduct(productId);
        productCommandHandler.handle(command);
    }
}
