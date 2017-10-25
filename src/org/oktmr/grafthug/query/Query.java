package org.oktmr.grafthug.query;

import org.oktmr.grafthug.query.model.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Query {
  private HashMap<String, String> prefixes;
  private HashMap<Integer, Variable> variables;
  private ArrayList<Condition> conditions;

  public Query() {
    prefixes = new HashMap<>();
    variables = new HashMap<>();
    conditions = new ArrayList<>();
  }

  public boolean addPrefix(String name, String namespace) {
    if (prefixes.containsKey(name)) {
      return false;
    }
    prefixes.put(name, namespace);
    return true;
  }

  public void addVariables(Variable variable) {
    variables.put(variable.getId(), variable);
  }

  public void addCondition(Condition condition) {
    conditions.add(condition);
  }
}
