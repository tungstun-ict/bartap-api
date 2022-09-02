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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    @Operation(
            summary = "Finds products of a bar",
            description = "Find all products of a bar with the given id, which can be filtered by: product type, category, favorites and any provided search term"
    )
    public List<ProductResponse> getAllProductsOfBar(
            @Parameter(description = "Id value of the bar") @PathVariable("barId") UUID barId,
            @Parameter(description = "(Optional) String value to filter on product type") @RequestParam(value = "productType", required = false) String productType,
            @Parameter(description = "(Optional) UUID value to filter on category with categoryId") @RequestParam(value = "categoryId", required = false) UUID categoryId,
            @Parameter(description = "(Optional) Boolean value to filter on favorites") @RequestParam(value = "onlyFavorites", required = false) Boolean onlyFavorites,
            @Parameter(description = "(Optional) String value of search term to filter on") @Valid @RequestParam(value = "searchText", required = false) String searchText
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
    @Operation(
            summary = "Finds product of a bar",
            description = "Find a product of a bar with the given id's"
    )
    public ProductResponse getProductOfBar(
            @Parameter(description = "Id value of the bar") @PathVariable("barId") UUID barId,
            @Parameter(description = "Id value of the product") @PathVariable("productId") UUID productId
    ) throws EntityNotFoundException {
        Product product = productQueryHandler.handle(new GetProduct(barId, productId));
        return converter.convert(product);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @Operation(
            summary = "Creates a new product",
            description = "Create a new product for a bar with the given information"
    )
    public UuidResponse addProductOfBar(
            @Parameter(description = "Id value of the bar") @PathVariable("barId") UUID barId,
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
    @Operation(
            summary = "Updates a product of a bar",
            description = "Update a product of a bar with the given information"
    )
    public UuidResponse updateProductOfBar(
            @Parameter(description = "Id value of the bar") @PathVariable("barId") UUID barId,
            @Parameter(description = "Id value of the product") @PathVariable("productId") UUID productId,
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
    @Operation(
            summary = "Deletes a product of a bar",
            description = "Delete a product of a bar with the given id's"
    )
    public void deleteProductFromBar(
            @Parameter(description = "Id value of the bar") @PathVariable("barId") UUID barId,
            @Parameter(description = "Id value of the product") @PathVariable("productId") UUID productId
    ) throws EntityNotFoundException {
        DeleteProduct command = new DeleteProduct(productId);
        productCommandHandler.handle(command);
    }
}
