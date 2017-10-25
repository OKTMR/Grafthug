package org.oktmr.grafthug.query.model;

public class Variable implements Value {
  private int id;
  private String name;
  private boolean isReturned;

  public Variable(int id, String name, boolean isReturned) {
    this.id = id;
    this.name = name;
    this.isReturned = isReturned;
  }

  public int getId() {
    return id;
  }

  public String stringValue() {
    return "?" + name;
  }
}
