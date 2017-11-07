package projgraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
     * @return id of Node
     */
    public int getId() {
        return id;
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
            HashSet<Node> node = entry.getValue();
            Edge edge = entry.getKey();
            Iterator<Node> iterator = node.iterator();
            while (iterator.hasNext()) {
                indexStructure.computeIfAbsent(iterator.next(), k -> new ArrayList<Edge>()).add(edge);
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
     * @param predicate Edge
     * @return Set of nodes
     */
    public ArrayList<Node> requestIndexStructure(Edge predicate) {
        ArrayList<Node> results = new ArrayList<>();
        for (Entry<Node, ArrayList<Edge>> value : indexStructure.entrySet()) {
            for (Edge e : value.getValue()) {
                if (e.equals(predicate)) {
                    results.add(value.getKey());
                }
            }
        }
        return results;
    }

    /**
     * @return pretty print of nodeId
     */
    public String toString() {
        return "n" + id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Node)) return false;

        Node n = (Node) obj;

        return this == n || id == n.id;
    }
}
