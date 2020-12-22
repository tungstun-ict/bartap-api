package com.tungstun.barapi.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class SessionFactory {
    private LocalDateTime date;

    public SessionFactory(LocalDateTime date) {
        this.date = date;
    }

    public Session createSession(){
        return new Session(this.date,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>());
    }
}
