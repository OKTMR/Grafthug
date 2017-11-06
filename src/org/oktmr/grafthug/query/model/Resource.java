package org.oktmr.grafthug.query.model;

public class Resource implements Value {
    protected String name;
    private int id;

    public Resource(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String stringValue() {
        return name;
    }
}
