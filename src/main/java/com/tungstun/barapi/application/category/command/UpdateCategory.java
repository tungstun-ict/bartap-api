package com.tungstun.barapi.application.category.command;

import java.util.UUID;

public record UpdateCategory(
        UUID barId,
        UUID categoryId,
        String name) {
}
