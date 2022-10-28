package com.tungstun.barapi.application.person.command;

public record ConnectUserToPerson(
        String username,
        String token) {
}
