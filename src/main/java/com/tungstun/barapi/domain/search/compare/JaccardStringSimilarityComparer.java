package com.tungstun.barapi.domain.search.compare;

import org.apache.commons.text.similarity.JaccardSimilarity;
import org.springframework.stereotype.Component;

@Component
public class JaccardStringSimilarityComparer implements StringComparer{
    private final JaccardSimilarity jaccardSimilarity = new JaccardSimilarity();

    @Override
    public Double apply(String left, String right) {
        return jaccardSimilarity.apply(left, right);
    }

    @Override
    public Double applyAll(String left, String... allRight) {
        double bestValue = 0d;
        for (String right : allRight) {
            Double value = apply(left, right);
            bestValue = bestValue > value? bestValue : value;
        }
        return bestValue;
    }
}
