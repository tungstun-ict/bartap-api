package com.tungstun.barapi.domain.session;

import java.util.ArrayList;
import java.util.UUID;

public class SessionFactory {
    private final String name;

    public SessionFactory(String name) {
        this.name = name;
    }

    public Session create() {
        return new Session(
                UUID.randomUUID(),
                name,
                new ArrayList<>()
        );
    }
}
