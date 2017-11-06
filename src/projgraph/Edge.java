package projgraph;

import java.util.*;

public class Edge {
	private int id;
	
	Edge(int id){
		this.id = id;
	}
	
	/**
	 * 
	 * @return pretty print of edgeId
	 */
	public String toString() {
		return "e" + id;
	}
}
