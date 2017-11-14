package org.oktmr.grafthug.query;

import org.oktmr.grafthug.query.model.Field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Query {
    private HashMap<String, String> prefixes;
    private ArrayList<Field> variables;
    private ArrayList<Condition> conditions;

    public Query() {
        prefixes = new HashMap<>();
        variables = new ArrayList<>();
        conditions = new ArrayList<>();
    }

    public void addPrefixes(HashMap<String, String> prefixes) {
        this.prefixes.putAll(prefixes);
    }

    public void addConditions(ArrayList<Condition> conditions) {
        this.conditions.addAll(conditions);
    }

    public void addVariables(ArrayList<Field> variables) {
        this.variables.addAll(variables);
    }

    /*Getters*/

    public HashMap<String, String> getPrefixes() {
        return prefixes;
    }

    public ArrayList<Condition> getConditions() {
        return conditions;
    }

    public ArrayList<Field> getVariables() {
        return variables;
    }


    @Override
    public String toString() {
        StringBuilder st = new StringBuilder();

        for (Map.Entry<String, String> entry : prefixes.entrySet()) {
            st.append("PREFIX\t").append(entry.getKey()).append(":\t").append(entry.getValue()).append("\r\n");
        }

        st.append("\r\nSELECT ");

        for (Field status : variables) {
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
