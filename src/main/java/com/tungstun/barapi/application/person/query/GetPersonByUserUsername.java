package com.tungstun.barapi.application.person.query;

import java.util.UUID;

public record GetPersonByUserUsername(
        UUID barId,
        UUID userId) {
}
