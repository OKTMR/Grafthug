package org.oktmr.grafthug.query.model;


public class Literal implements Value{
  private String value;

  public Literal(String value){
    this.value = value;
  }

  public String stringValue(){
    return value;
  }
}
