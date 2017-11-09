package projgraph;

import java.util.ArrayList;
import java.util.HashMap;

public class DataStore {

    public HashMap<String, Integer> valueIndexes;
    public ArrayList<String> values;

    public DataStore() {
        valueIndexes = new HashMap<>();
        values = new ArrayList<>();
    }

    /**
     * Add index of subject and object to Node
     * Add index of predicate to Edge
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

}
