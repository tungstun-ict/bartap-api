package com.tungstun.barapi.application.person.query;

import java.util.UUID;

public record GetPersonByUserUsername(
        UUID barId,
        String username) {
}
