package org.oktmr.grafthug;

import java.util.ArrayList;
import java.util.HashMap;

public class DataStore {

    public HashMap<String, Integer> valueIndexes;
    public ArrayList<String> values;
    public String[] valuesOpti;

    public DataStore() {
        valueIndexes = new HashMap<>();
        values = new ArrayList<>();
        valuesOpti = new String[0];
    }

    /**
     * Add index of subject and object to TreeNode
     * Add index of predicate to TreeEdge
     * Can be used as a getter to translate query in nodeId/edgeId
     *
     * @param value of subject, object or predicate
     * @return index of value
     */
    public int add(String value) {
        if (!valueIndexes.containsKey(value)) {
            int index = values.size(); //auto-increment yolo
            valueIndexes.put(value, index);
            values.add(value);
            return index;
        }
        return valueIndexes.get(value);
    }

    public void optimize() {
        valueIndexes = new HashMap<>(valueIndexes);
        valuesOpti = values.toArray(new String[0]);
        values = null;
    }

    /**
     * get the value of an id
     * to return query result
     *
     * @param id, id of a node or edge
     * @return value of index
     */
    public String getValue(int id) {
        return valuesOpti[id];
    }

    public Integer getIndex(String value) {
        return valueIndexes.get(value);
    }

}
