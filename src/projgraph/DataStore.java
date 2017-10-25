package projgraph;

import java.util.*;

public class DataStore {

	public HashMap<String, Integer> valueIndexes;
	public ArrayList<String> values;
	
	public DataStore() {
		valueIndexes = new HashMap<>();
		values = new ArrayList<>();
	}

	public int add(String value) {
		if (!valueIndexes.containsKey(value)) {
			int index = values.size(); //auto-increment yolo
			valueIndexes.put(value, index);
			values.add(value);
			return index;
		}
		return valueIndexes.get(value);
	}
	
	
}
