package com.tungstun.barapi.domain.search;

import com.tungstun.barapi.domain.product.Product;

import java.util.List;

public interface ProductSearchAlgorithm {
    List<Product> apply(List<Product> products, String searchText);
}
