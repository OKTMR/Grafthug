package org.oktmr.grafthug.query.model;

/**
 *
 */
public class Field<T extends Value> implements Value {
    private int id;
    private String name;
    private T value;

    private int hash;

    public Field(T value) {
        this.name = null;
        this.value = value;

        hash = value.hashCode();
    }

    public Field(int id, String name) {
        this.value = null;
        this.name = name;
        this.id = id;

        hash = name.hashCode();
    }

    public static String getVariableName(String var) {
        return isVariable(var) ? var.substring(1) : var;
    }

    public static boolean isVariable(String var) {
        return var.charAt(0) == '?';
    }

    public boolean isVariable() {
        return name != null;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String stringValue() {
        return name != null ? "?" + name + "(" + id + ")" : value.stringValue();

    }

    @Override
    public String toString() {
        return this.stringValue();
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Field)) return false;

        Field fd = (Field) obj;

        if (this == fd) return true;

        if (name == null && value.equals(fd.value)) return true;
        else if (id == fd.id) return true;

        return false;
    }
}
