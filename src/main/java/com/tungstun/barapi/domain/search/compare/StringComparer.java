package com.tungstun.barapi.domain.search.compare;

public interface StringComparer {
    /**
     * Compares left string to right string.
     * Returns  Double : best compared index for implemented strategy
     */
    Double apply(String left, String right) ;

    /**
     * Compares left string to all right strings.
     * Returns  Double : best compared index of implemented strategy
     */
    Double applyAll(String left, String ...right) ;
}
