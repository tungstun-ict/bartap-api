package com.tungstun.barapi.application.person.command;

import java.util.UUID;

public record CreatePerson(
        UUID barId,
        String name) {
}
