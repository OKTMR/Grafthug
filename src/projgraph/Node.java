package projgraph;

import java.util.*;

public class Node {
	private int id;
	public HashMap<Edge, Node> linkIn; //edge, nodeIn = object
	public HashMap<Edge, Node> linkOut; //edge, nodeOut = subject
	
	Node(int id){
		this.id = id;
		linkIn = new HashMap<>();
		linkOut = new HashMap<>();
	}
	/**
	 * this is a object
	 * nodeOut is a subject
	 */
	public void addLinkOut (Edge edge, Node nodeOut){
		linkOut.put(edge, nodeOut);
	}
	
	/**
	 * this is a subject
	 * nodeIn is a object
	 */
	public void addLinkIn (Edge edge, Node nodeIn){
		linkIn.put(edge, nodeIn);
	}
}
