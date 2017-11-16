package org.oktmr.grafthug;

import gnu.trove.map.hash.TObjectIntHashMap;

import java.util.ArrayList;

public class DataStore {

    public TObjectIntHashMap<String> valueIndexes;
    public ArrayList<String> values;

    public DataStore() {
        valueIndexes = new TObjectIntHashMap<>(10, 0.75f, -1);
        values = new ArrayList<>();
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

    /**
     * get the value of an id
     * to return query result
     *
     * @param id, id of a node or edge
     * @return value of index
     */
    public String getValue(int id) {
        return values.get(id);
    }

    public int getIndex(String value) {
        return valueIndexes.get(value);
    }

    public void compact() {
        valueIndexes.compact();
    }
}
