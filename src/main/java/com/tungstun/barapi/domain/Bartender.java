package com.tungstun.barapi.domain;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "bartender")
@PrimaryKeyJoinColumn(name = "user_id")
public class Bartender extends Person {
    public Bartender() { super(); }
    public Bartender(String name) {
        super(name);
    }
}
