package com.valkyrie.catalog.model;

import javax.persistence.Embeddable;

@Embeddable
public class Instructor {
    private String id;
    private String name;
    private String email;

    // Constructors
    public Instructor() {}

    public Instructor(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}