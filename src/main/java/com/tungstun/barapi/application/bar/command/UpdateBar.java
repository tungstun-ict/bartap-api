package com.tungstun.barapi.application.bar.command;

import java.util.UUID;

public record UpdateBar(
        UUID barId,
        String address,
        String name,
        String mail,
        String phoneNumber) {
}
