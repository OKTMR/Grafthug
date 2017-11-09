package projgraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

public class Node {
    public HashMap<Edge, HashSet<Node>> linkIn; //edge, nodeIn = object
    public HashMap<Edge, HashSet<Node>> linkOut; //edge, nodeOut = subject
    public HashMap<Node, ArrayList<Edge>> indexStructure = new HashMap<>(); //yolo
    private int id; //index

    Node(int id) {
        this.id = id;
        linkIn = new HashMap<>();
        linkOut = new HashMap<>();
    }

    /**
     * this is a object
     * nodeOut is a subject
     */
    public void addLinkOut(Edge edge, Node nodeOut) {
        linkOut.computeIfAbsent(edge, k -> new HashSet<Node>()).add(nodeOut);
    }

    /**
     * this is a subject
     *
     * @param edge   is edge between the nodes
     * @param nodeIn is a object
     */
    public void addLinkIn(Edge edge, Node nodeIn) {
        linkIn.computeIfAbsent(edge, k -> new HashSet<Node>()).add(nodeIn);
    }

    /**
     * Create an indexstructure from linkOut
     *
     * @return indexed graph of node
     */
    public void createIndex() {
        for (Entry<Edge, HashSet<Node>> entry : linkOut.entrySet()) {
            for (Node node : entry.getValue()) {
                indexStructure.computeIfAbsent(node, k -> new ArrayList<>()).add(entry.getKey());
            }
        }
    }

    /**
     * @return indexed graph of node
     */
    public HashMap<Node, ArrayList<Edge>> getIndexStructure() {
        return indexStructure;
    }

    /**
     * @return pretty print of nodeId
     */
    public String toString() {
        return "n" + id;
    }
}
