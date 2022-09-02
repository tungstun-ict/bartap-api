package com.tungstun.security.config.filter;

import java.util.UUID;

public record Authorization(UUID barId, String role) {
}
