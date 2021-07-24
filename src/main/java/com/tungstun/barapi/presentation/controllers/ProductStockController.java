package com.tungstun.barapi.presentation.controllers;

import com.tungstun.barapi.application.StockService;
import com.tungstun.barapi.domain.stock.Stock;
import com.tungstun.barapi.presentation.dto.request.StockRequest;
import com.tungstun.barapi.presentation.dto.response.StockResponse;
import com.tungstun.barapi.presentation.mapper.ResponseMapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/bars/{barId}/products/{productId}/stock")
public class ProductStockController {
    private final StockService stockService;
    private final ResponseMapper responseMapper;

    public ProductStockController(StockService stockService, ResponseMapper responseMapper) {
        this.stockService = stockService;
        this.responseMapper = responseMapper;
    }

    private StockResponse convertToStockResponse(Stock stock) {
        return responseMapper.convert(stock, StockResponse.class);
    }

    @GetMapping
    @PreAuthorize("hasPermission(#barId, 'ROLE_BAR_OWNER')")
    @ApiOperation(
            value = "Gets product's stock",
            notes = "Provide id of bar and product to find the stock",
            response = StockResponse.class
    )
    public ResponseEntity<StockResponse> getProductsStock(
            @ApiParam(value = "ID value for the bar you want to retrieve the stock from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the product you want to retrieve the stock of") @PathVariable("productId") Long productId
    ) throws NotFoundException {
        Stock stock = this.stockService.getStock(barId, productId);
        return new ResponseEntity<>(convertToStockResponse(stock), HttpStatus.OK);
    }

    @PatchMapping("/increase")
    @PreAuthorize("hasPermission(#barId, 'ROLE_BAR_OWNER')")
    @ApiOperation(
            value = "Increases product's stock",
            notes = "Provide id of bar and product to increase the stock",
            response = StockResponse.class
    )
    public ResponseEntity<StockResponse> increaseStockOfProduct(
            @ApiParam(value = "ID value for the bar you want to increase the stock from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the product you want to increase the stock of") @PathVariable("productId") Long productId,
            @Valid @RequestBody StockRequest stockRequest
    ) throws NotFoundException {
        Stock stock = this.stockService.increaseStock(barId, productId, stockRequest);
        return new ResponseEntity<>(convertToStockResponse(stock), HttpStatus.OK);
    }

    @PatchMapping("/decrease")
    @PreAuthorize("hasPermission(#barId, 'ROLE_BAR_OWNER')")
    @ApiOperation(
            value = "Decreases product's stock",
            notes = "Provide id of bar and product to decrease the stock",
            response = StockResponse.class
    )
    public ResponseEntity<StockResponse> decreaseStockOfProduct(
            @ApiParam(value = "ID value for the bar you want to decrease the stock from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the product you want to decrease the stock of") @PathVariable("productId") Long productId,
            @Valid @RequestBody StockRequest stockRequest
    ) throws NotFoundException {
        Stock stock = this.stockService.decreaseStock(barId, productId, stockRequest);
        return new ResponseEntity<>(convertToStockResponse(stock), HttpStatus.OK);
    }

    @PatchMapping("/update")
    @PreAuthorize("hasPermission(#barId, 'ROLE_BAR_OWNER')")
    @ApiOperation(
            value = "Updates product's stock",
            notes = "Provide id of bar and product to update the stock",
            response = StockResponse.class
    )
    public ResponseEntity<StockResponse> updateStockOfProduct(
            @ApiParam(value = "ID value for the bar you want to update the stock from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the product you want to update the stock of") @PathVariable("productId") Long productId,
            @Valid @RequestBody StockRequest stockRequest
    ) throws NotFoundException {
        Stock stock = this.stockService.updateStockAmount(barId, productId, stockRequest);
        return new ResponseEntity<>(convertToStockResponse(stock), HttpStatus.OK);
    }
}
