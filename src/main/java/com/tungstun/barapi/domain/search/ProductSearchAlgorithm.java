package com.tungstun.barapi.domain.search;

import com.tungstun.barapi.domain.product.Product;

import java.util.Collection;

public interface ProductSearchAlgorithm {
    Collection<Product> apply(Collection<Product> products, String searchText);
}
