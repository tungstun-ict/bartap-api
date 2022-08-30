package com.tungstun.barapi.port.web.product.converter;

import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.port.web.product.response.ProductResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductConverter {
    public ProductResponse convert(Product product) {
        ProductResponse response =  new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setSize(product.getSize());
        response.setPrice(product.getPrice().amount().doubleValue());
        response.setFavorite(product.isFavorite());
        response.setBrand(product.getBrand());
        response.setCategory(product.getCategory());
        return response;
    }

    public List<ProductResponse> convertAll(List<Product> products) {
        return products.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
