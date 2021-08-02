package com.tungstun.barapi.presentation.converter;

import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarDetails;
import com.tungstun.barapi.presentation.dto.response.BarResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BarConverter {
    public BarResponse convert(Bar bar) {
        BarResponse response =  new BarResponse();
        response.setId(bar.getId());
        BarDetails details = bar.getDetails();
        response.setName(details.getName());
        response.setPhoneNumber(details.getPhoneNumber());
        response.setMail(details.getMail());
        response.setAddress(details.getAddress());
        return response;
    }

    public List<BarResponse> convertAll(List<Bar> bars) {
        return bars.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
