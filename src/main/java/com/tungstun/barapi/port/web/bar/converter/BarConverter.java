package com.tungstun.barapi.port.web.bar.converter;

import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.port.web.bar.response.BarResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BarConverter {
    public BarResponse convert(Bar bar) {
        return new BarResponse(
                bar.getId(),
                bar.getDetails().getAddress(),
                bar.getDetails().getName(),
                bar.getDetails().getMail(),
                bar.getDetails().getPhoneNumber()
        );
    }

    public List<BarResponse> convertAll(List<Bar> bars) {
        return bars.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
