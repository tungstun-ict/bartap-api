package com.tungstun.barapi.application.session.query;

import java.util.UUID;

public record GetSession(
        UUID barId,
        UUID sessionId) {
}
