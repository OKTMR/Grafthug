package projgraph;

import java.util.ArrayList;
import java.util.HashMap;

public class Dictionnaire {

    public HashMap<Integer, Node> nodes; //subjects, objects
    public HashMap<Integer, Edge> edges; //predicates

    Dictionnaire() {
        nodes = new HashMap<>();
        edges = new HashMap<>();
    }

    /**
     * Add index of subject and object to Node
     * Add index of predicate to Edge
     */
    public void add(int subject, int predicate, int object) {
        // subject = node out
        // object = node in
        // predicate = edge
        Node nodeOut = addNode(subject);
        Node nodeIn = addNode(object);
        Edge edge = addEdge(predicate);
        nodeOut.addLinkIn(edge, nodeIn);
        nodeIn.addLinkOut(edge, nodeOut);
    }

    /**
     * Create Node if index of node doesn't exist with index
     * Add new Node to HashMap nodes
     * Return new node or existing node
     */
    public Node addNode(int nodeId) {
        if (nodes.containsKey(nodeId)) {
            return nodes.get(nodeId);
        } else {
            Node node = new Node(nodeId);
            nodes.put(nodeId, node);
            return node;
        }
    }

    /**
     * Create Edge if index of edge doesn't exist with index
     * Add new Edge to HashMap edges
     * Return new Edge or existing edge
     */
    public Edge addEdge(int edgeId) {
        if (edges.containsKey(edgeId)) {
            return edges.get(edgeId);
        } else {
            Edge edge = new Edge(edgeId);
            edges.put(edgeId, edge);
            return edge;
        }
    }

    /**
     * Call Node.createIndex() for each node of HashMap nodes to get each neighbords of a node.
     */
    public void index() {
        for (Node node : nodes.values()) {
            node.createIndex();
            //System.out.print("IndexStructure of Node " + node + " : ");
            //System.out.println(node.getIndexStructure());
        }
    }
}
