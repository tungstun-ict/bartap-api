package com.tungstun.barapi.application.bar.command;

public record CreateBar(
        String address,
        String name,
        String mail,
        String phoneNumber,
        String ownerUsername) {
}
