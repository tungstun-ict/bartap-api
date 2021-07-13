package com.tungstun.barapi.application;

import java.util.List;

public interface StringSimilarityStrategy<T>  {
    Double apply(T entity, String compareString);
    List<T> filterList(List<T> entities, String compareString);
    List<T> filterList(List<T> entities, String compareString, Double threshold);
}
