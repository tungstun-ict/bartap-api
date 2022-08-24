package com.tungstun.barapi.application.person.query;

import java.util.UUID;

public record GetPersonByUserUsername(
        String username,
        UUID barId) {
}
