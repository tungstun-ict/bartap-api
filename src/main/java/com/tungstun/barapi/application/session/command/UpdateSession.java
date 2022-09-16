package com.tungstun.barapi.application.session.command;

import java.util.UUID;

public record UpdateSession(
        UUID barId,
        UUID sessionId,
        String name) {
}
