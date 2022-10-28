//package com.tungstun.barapi.domain.search.engine.product;
//
//import com.tungstun.barapi.domain.product.Product;
//import com.tungstun.barapi.domain.search.compare.StringComparer;
//import com.tungstun.barapi.domain.search.engine.SearchEngine;
//import org.springframework.stereotype.Component;
//
//import java.util.*;
//import java.util.regex.Pattern;
//import java.util.stream.Collectors;
//
//@Component
//public class ProductSearchEngine implements SearchEngine<Product> {
//    private static final Double THRESHOLD = 0.25;
//    private final StringComparer stringComparer;
//    private Pattern regexPattern;
//
//    public ProductSearchEngine(StringComparer stringComparer) {
//        this.stringComparer = stringComparer;
//    }
//
//    @Override
//    public List<Product> search(Collection<Product> source, String searchString) {
//        String regex = "(.*)" + searchString + "(.*)";
//        this.regexPattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
//        Map<Product, Double> filteredProducts = new HashMap<>();
//        for (Product product : source) {
//            Double searchIndex = this.calculateSearchIndex(product, searchString);
//            if (searchIndex >= THRESHOLD) {
//                filteredProducts.put(product, searchIndex);
//            }
//        }
//        return toList(filteredProducts);
//    }
//
//    private Double calculateSearchIndex(Product product, String searchString) {
//        String completeName = product.getBrand() + " " + product.getName();
//        String brand = product.getBrand();
//        String name = product.getName();
//        Double searchIndex = stringComparer.applyAll(searchString, completeName, brand, name);
//        searchIndex = this.compareToPattern(completeName, searchIndex);
//        return searchIndex;
//    }
//
//    private Double compareToPattern(String input, Double searchIndex) {
//        boolean matchesRegex = regexPattern.matcher(input).matches();
//        if (matchesRegex) {
//            searchIndex *= 1.4;
//        }
//        return searchIndex;
//    }
//
//    private List<Product> toList(Map<Product, Double> map) {
//        return new ArrayList<>(map.entrySet().stream()
//                .sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue()))
//                .collect(Collectors.toMap(
//                        Map.Entry::getKey,
//                        Map.Entry::getValue,
//                        (a, b) -> {
//                            throw new AssertionError();
//                        },
//                        LinkedHashMap::new
//                )).keySet());
//    }
//}
