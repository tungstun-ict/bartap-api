package com.tungstun.barapi.application.person.command;

import java.util.UUID;

public record UpdatePerson(
        UUID barId,
        UUID personId,
        String name) {
}
