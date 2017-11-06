package org.oktmr.grafthug.query;

import org.oktmr.grafthug.query.model.Field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Query {
    private HashMap<String, String> prefixes;
    private HashMap<Integer, Field> variables;
    private ArrayList<Field> fields;
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

    public void addPrefixes(HashMap<String, String> prefixes) {
        this.prefixes.putAll(prefixes);
    }


    public void addCondition(Condition condition) {
        conditions.add(condition);
    }

    public void addConditions(ArrayList<Condition> conditions) {
        this.conditions.addAll(conditions);
    }

    public void addVariables(HashMap<Integer, Field> variables) {
        this.variables.putAll(variables);
    }
    
    /*Getters*/
    
    public HashMap<String, String> getPrefixes() {
        return prefixes;
    }


    public ArrayList<Condition> getConditions() {
        return conditions;
    }

    public HashMap<Integer, Field> getVariables() {
        return variables;
    }
    
    

    @Override
    public String toString() {
        StringBuilder st = new StringBuilder();

        for (Map.Entry<String, String> entry : prefixes.entrySet()) {
            st.append("PREFIX\t").append(entry.getKey()).append(":\t").append(entry.getValue()).append("\n\r");
        }

        st.append("\n\rSELECT ");
        for (Field status : variables.values()) {
            st.append(status.stringValue()).append(" ");
        }
        st.append("WHERE {\r\n");

        for (Condition condition : conditions) {
            st.append(condition.toString()).append(" .\r\n");
        }
        st.append("}");

        return st.toString();
    }
}
