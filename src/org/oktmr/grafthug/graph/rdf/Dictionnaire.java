package org.oktmr.grafthug.graph.rdf;

import java.util.HashMap;

public class Dictionnaire {

    public HashMap<Integer, RdfNode> nodes; //subjects, objects
    public HashMap<Integer, RdfEdge> edges; //predicates

    public Dictionnaire() {
        nodes = new HashMap<>();
        edges = new HashMap<>();
    }


    public void clear() {
        nodes = null;
        edges = null;
    }

    /**
     * Add index of subject and object to TreeNode
     * Add index of predicate to TreeEdge
     */
    public void add(int subject, int predicate, int object) {
        // subject = node out
        // object = node in
        // predicate = edge
        RdfNode rdfNodeOut = addNode(subject);
        RdfNode rdfNodeIn = addNode(object);
        RdfEdge edge = addEdge(predicate);
        rdfNodeOut.addLinkIn(edge, rdfNodeIn);
        rdfNodeIn.addLinkOut(edge, rdfNodeOut);
    }

    /**
     * Create TreeNode if index of node doesn't exist with index
     * Add new TreeNode to HashMap treeNodes
     * Return new node or existing node
     */
    public RdfNode addNode(int nodeId) {
        if (nodes.containsKey(nodeId)) {
            return nodes.get(nodeId);
        } else {
            RdfNode rdfNode = new RdfNode(nodeId);
            nodes.put(nodeId, rdfNode);
            return rdfNode;
        }
    }

    /**
     * Create TreeEdge if index of edge doesn't exist with index
     * Add new TreeEdge to HashMap edges
     * Return new TreeEdge or existing edge
     */
    public RdfEdge addEdge(int edgeId) {
        if (edges.containsKey(edgeId)) {
            return edges.get(edgeId);
        } else {
            RdfEdge edge = new RdfEdge(edgeId);
            edges.put(edgeId, edge);
            return edge;
        }
    }

    /**
     * Call TreeNode.createIndex() for each node of HashMap treeNodes to get each neighbords of a node.
     */
    public void index() {
        for (RdfNode rdfNode : nodes.values()) {
            rdfNode.createIndex();
            //System.out.print("IndexStructure of TreeNode " + rdfNode + " : ");
            //System.out.println(rdfNode.getIndexStructure());
        }
    }

    public HashMap<Integer, RdfNode> getNodes() {
        return nodes;
    }

    public RdfNode getNode(int index) {
        return nodes.get(index);
    }

    public HashMap<Integer, RdfEdge> getEdges() {
        return edges;
    }

    public RdfEdge getEdge(int index) {
        return edges.get(index);
    }
}
