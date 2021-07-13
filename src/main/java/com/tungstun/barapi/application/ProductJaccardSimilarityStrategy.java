package com.tungstun.barapi.application;

import com.tungstun.barapi.domain.product.Product;
import org.apache.commons.text.similarity.JaccardSimilarity;

import java.util.*;
import java.util.stream.Collectors;

public class ProductJaccardSimilarityStrategy implements StringSimilarityStrategy<Product> {
    private final Double DEFAULT_THRESHOLD = 0.25;
    private final JaccardSimilarity JACCARD_SIMILARITY = new JaccardSimilarity();

    public ProductJaccardSimilarityStrategy() {
    }

    @Override
    public Double apply(Product product, String compareString) {
        String completeName = String.format("%s %s", product.getName(), product.getBrand());
        String brand = product.getBrand();
        String name = product.getName();

        Double value = JACCARD_SIMILARITY.apply(compareString, completeName);
        Double brandValue = JACCARD_SIMILARITY.apply(compareString, brand);
        Double nameValue = JACCARD_SIMILARITY.apply(compareString, name);

        value = Math.max(value, Math.max(brandValue, nameValue));
        return value;
    }

    @Override
    public List<Product> filterList(List<Product> entities, String compareString) {
        return filterList(entities, compareString, DEFAULT_THRESHOLD);
    }

    @Override
    public List<Product> filterList(List<Product> entities, String compareString, Double threshold) {
        Map<Product, Double> filteredProduct = new HashMap<>();
        for (Product product : entities) {
            Double jaccardSimilarityValue = this.apply(product, compareString);
            if (jaccardSimilarityValue >= threshold) filteredProduct.put(product, jaccardSimilarityValue);
        }
        return mapToList(filteredProduct);
    }

    private List<Product> mapToList(Map<Product, Double> map) {
        return new ArrayList<>(map.entrySet().stream()
                .sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> { throw new AssertionError(); },
                        LinkedHashMap::new
                )).keySet());
    }

}
