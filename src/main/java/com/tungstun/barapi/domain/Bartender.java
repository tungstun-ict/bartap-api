package com.tungstun.barapi.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "bartender")
@PrimaryKeyJoinColumn(name = "user_id")
public class Bartender extends Person {
    @JsonBackReference
    @ManyToMany(
            cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}
    )
    @JoinTable(
            name = "session_bartender",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "id") }
    )
    public List<Session> shifts;

    public Bartender() { super(); }
    public Bartender(String name) {
        super(name);
    }

    public boolean addShift(Session session) {
        if ( !this.shifts.contains(session) ) return this.shifts.add(session);
        return false;
    }

    public boolean removeShift(Session session){
        return this.shifts.remove(session);
    }
}
