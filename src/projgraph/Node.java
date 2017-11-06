package projgraph;

import java.util.*;
import java.util.Map.Entry;

public class Node {
	private int id; //index
	public HashMap<Edge, HashSet<Node>> linkIn; //edge, nodeIn = object
	public HashMap<Edge, HashSet<Node>> linkOut; //edge, nodeOut = subject
	public HashMap<Node, ArrayList<Edge>> indexStructure = new HashMap<>(); //yolo
	
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
		linkOut.computeIfAbsent(edge, k->new HashSet<Node>()).add(nodeOut);
	}
	
	/**
	 * this is a subject
	 * @param edge is edge between the nodes
	 * @param nodeIn is a object
	 */
	public void addLinkIn (Edge edge, Node nodeIn){
		linkIn.computeIfAbsent(edge, k->new HashSet<Node>()).add(nodeIn);
	}
	
	/**
	 * Create an indexstructure from linkOut
	 * @return indexed graph of node
	 */
	public void createIndex(){
		for(Entry<Edge,HashSet<Node>> entry : linkOut.entrySet()){
	        HashSet<Node> node = entry.getValue();
	    	Edge edge = entry.getKey();
	    	Iterator<Node> iterator = node.iterator();
	    	while(iterator.hasNext()){
	    		indexStructure.computeIfAbsent(iterator.next(), k-> new ArrayList<Edge>()).add(edge);
	    	}
	    }
	}
	
	/**
	 * 
	 * @return indexed graph of node
	 */
	public HashMap<Node, ArrayList<Edge>> getIndexStructure(){
		return indexStructure;
	}
	
	/**
	 * 
	 * @return pretty print of nodeId
	 */
	public String toString() {
		return "n" + id;
	}
}
