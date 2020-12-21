package com.tungstun.barapi.presentation.mapper;

import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ResponseMapper extends ModelMapper{
    ModelMapper mapper;

    public ResponseMapper() {
        this.mapper = new ModelMapper();


    }

    public <S, T> T convert(S source, Class<T> targetClass) {
        return mapper.map(source, targetClass);
    }

    public <S, T> List<T> convertList(List<S> source, Class<T> targetClass) {
        return source
                .stream()
                .map( element -> mapper.map(element, targetClass) )
                .collect(Collectors.toList());
    }
}
