//package com.tungstun.barapi.domain.search.compare;
//
//import com.tungstun.barapi.domain.search.ProductSimilaritySearchAlgorithm;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.Arguments;
//import org.junit.jupiter.params.provider.MethodSource;
//
//import java.util.stream.Stream;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//class JaccardStringSimilarityComparerTest {
//    private ProductSimilaritySearchAlgorithm algorithm;
//
//    private static Stream<Arguments> provideArgs() {
//        return Stream.of(
//                Arguments.of("word", "word", 1d),
//                Arguments.of("word", "ape", 0d),
//                Arguments.of("word", "wordword", 1d),
//                Arguments.of("word", "wore", 0.6d),
//                Arguments.of("word", "wope", 0.3333333333333333d),
//                Arguments.of("word", "wor", 0.75d),
//                Arguments.of("word", "wop", 0.4d),
//                Arguments.of("word", "wod", 0.75d)
//        );
//    }
//
//    @BeforeEach
//    void setUp() {
//        algorithm = new ProductSimilaritySearchAlgorithm();
//    }
//
//    @ParameterizedTest
//    @MethodSource("provideArgs")
//    @DisplayName("apply jaccard similarity equal")
//    void apply(String left, String right, Double expectedIndex) {
//        Double index = algorithm.apply(left, right);
//
//        assertEquals(expectedIndex, index);
//    }
//
//    @Test
//    @DisplayName("apply jaccard similarity to all")
//    void applyAll() {
//        Double index = algorithm.appl("word", "ape", "word", "worp");
//
//        assertEquals(1d, index);
//    }
//
//    @Test
//    @DisplayName("apply jaccard similarity no match")
//    void applyAllNoMatch() {
//        Double index = algorithm.applyAll("word", "ape", "pp", "lazy");
//
//        assertEquals(0d, index);
//    }
//}