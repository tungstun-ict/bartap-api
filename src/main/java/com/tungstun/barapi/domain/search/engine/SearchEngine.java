package com.tungstun.barapi.domain.search.engine;

import java.util.Collection;
import java.util.List;

public interface SearchEngine<T> {
    /**
     * Uses search strategy to search for source item(s) with searchString
     */
    List<T> search(Collection<T> source, String searchString);
}
