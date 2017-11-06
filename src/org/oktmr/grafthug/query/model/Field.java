package org.oktmr.grafthug.query.model;

/**
 *
 */
public class Field<T extends Value> implements Value {
    private String name;
    private T value;

    public Field(T value) {
        this.name = null;
        this.value = value;
    }

    public Field(String name) {
        this.value = null;
        this.name = name;
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

    @Override
    public String stringValue() {
        return name != null ? "?" + name : value.stringValue();

    }

    @Override
    public String toString() {
        return this.stringValue();
    }

}
