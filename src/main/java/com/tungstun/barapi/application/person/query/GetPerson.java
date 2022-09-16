package com.tungstun.barapi.application.person.query;

import java.util.UUID;

public record GetPerson(
        UUID barId,
        UUID personId) {
}
