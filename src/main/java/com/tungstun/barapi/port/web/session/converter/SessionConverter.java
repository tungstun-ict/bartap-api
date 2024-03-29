package com.tungstun.barapi.port.web.session.converter;

import com.tungstun.barapi.domain.session.Session;
import com.tungstun.barapi.port.web.bill.converter.BillConverter;
import com.tungstun.barapi.port.web.session.response.SessionResponse;
import com.tungstun.barapi.port.web.session.response.SessionSummaryResponse;
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
                billConverter.convertAllToSummary(session.getBills())
        );
    }

    public List<SessionResponse> convertAll(List<Session> sessions) {
        return sessions.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    public SessionSummaryResponse convertToSummary(Session session) {
        return new SessionSummaryResponse(
                session.getId(),
                session.getName(),
                session.getCreationDate()
        );
    }

    public List<SessionSummaryResponse> convertAllToSummary(List<Session> sessions) {
        return sessions.stream()
                .map(this::convertToSummary)
                .collect(Collectors.toList());
    }
}
