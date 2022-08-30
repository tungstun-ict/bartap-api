package com.tungstun.barapi.port.web.session.converter;

import com.tungstun.barapi.domain.session.Session;
import com.tungstun.barapi.port.web.bill.converter.BillConverter;
import com.tungstun.barapi.port.web.session.response.SessionResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SessionConverter {
    private final BillConverter billConverter;

    public SessionConverter(BillConverter billConverter) {
        this.billConverter = billConverter;
    }

    public SessionResponse convert(Session session) {
        return new SessionResponse(
                session.getId(),
                session.getName(),
                session.getCreationDate(),
                session.getEndDate(),
                session.isActive(),
                billConverter.convertAll(session.getBills())
        );
    }

    public List<SessionResponse> convertAll(List<Session> sessions) {
        return sessions.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
