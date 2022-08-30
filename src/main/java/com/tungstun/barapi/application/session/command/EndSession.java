package com.tungstun.barapi.application.session.command;

import java.util.UUID;

public record EndSession(
        UUID barId,
        UUID sessionId) {
}
