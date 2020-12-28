package com.tungstun.barapi.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "bartender")
public class Bartender extends Person {
    public Bartender() { super(); }
    public Bartender(Long id, String name) {
        super(id, name);
    }
}
