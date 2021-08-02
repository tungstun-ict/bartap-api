package com.tungstun.barapi.presentation.converter;

import com.tungstun.barapi.domain.session.Session;
import com.tungstun.barapi.presentation.dto.response.SessionResponse;
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
        SessionResponse response =  new SessionResponse();
        response.setId(session.getId());
        response.setName(session.getName());
        response.setLocked(session.isLocked());
        response.setCreationDate(session.getCreationDate());
        response.setClosedDate(session.getClosedDate());
        response.setBills(billConverter.convertAll(session.getBills()));
        return response;
    }

    public List<SessionResponse> convertAll(List<Session> sessions) {
        return sessions.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
