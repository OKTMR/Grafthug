package org.oktmr.grafthug.query;

import org.oktmr.grafthug.query.model.*;

public class Condition {
  private Value subject;
  private Value predicate;
  private Value object;

  public String toString() {
    return subject.stringValue() + " " + predicate.stringValue() + " " + object.stringValue();
  }
}
