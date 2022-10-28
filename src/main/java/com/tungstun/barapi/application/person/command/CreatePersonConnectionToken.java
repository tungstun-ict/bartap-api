package com.tungstun.barapi.application.person.command;

import java.util.UUID;

public record CreatePersonConnectionToken(
        UUID barId,
        UUID personId) {
}
