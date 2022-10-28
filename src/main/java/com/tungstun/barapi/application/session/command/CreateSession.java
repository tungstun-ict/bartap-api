package com.tungstun.barapi.application.session.command;

import java.util.UUID;

public record CreateSession(
        UUID barId,
        String name) {
}
