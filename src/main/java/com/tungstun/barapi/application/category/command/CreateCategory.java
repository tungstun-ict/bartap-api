package com.tungstun.barapi.application.category.command;

import java.util.UUID;

public record CreateCategory(
        UUID barId,
        String name) {
}
