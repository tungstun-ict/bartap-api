package com.tungstun.barapi.presentation.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**\
 * Adapted from: https://www.baeldung.com/java-modelmapper-lists
 * Date: May 11th 2020
 * Author: Baeldung
 *
 * Model mapper class that is used to convert domain classes into
 * Data Transfer Object classes to send back to the user
 */
@Component
public class ResponseMapper extends ModelMapper{

    public ResponseMapper() { }

    /**
     * Converts the given source object into the requested target class
     * @param source object to be changed
     * @param target class to change source object into
     * @param <S> source class
     * @param <T> target class
     * @return converted object of class {@param <T>}
     */
    public <S, T> T convert(S source, Class<T> target) {
        return map(source, target);
    }

    /**
     * Converts each item from the list of objects to the give targetClass
     * @param source list of objects to be changed
     * @param target class to change source objects into
     * @param <S> Source class
     * @param <T> Target class
     * @return list of converted objects of class {@param <T>}
     */
    public <S, T> List<T> convertList(List<S> source, Class<T> target) {
        return source
                .stream()
                .map( element -> map(element, target) )
                .collect(Collectors.toList());
    }
}
