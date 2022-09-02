package com.tungstun.barapi.port.web.product.converter;

import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.port.web.product.response.ProductResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductConverter {
    public ProductResponse convert(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getBrand(),
                product.getSize(),
                product.getPrice().amount().doubleValue(),
                product.isFavorite(),
                product.getCategory()
        );
    }

    public List<ProductResponse> convertAll(List<Product> products) {
        return products.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
