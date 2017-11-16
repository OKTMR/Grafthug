package org.oktmr.grafthug.graph.rdf;

import gnu.trove.map.hash.TIntObjectHashMap;

public class Dictionnaire {

    public TIntObjectHashMap<RdfNode> nodes; //subjects, objects
    public TIntObjectHashMap<RdfEdge> edges; //predicates

    public Dictionnaire() {
        nodes = new TIntObjectHashMap<>();
        edges = new TIntObjectHashMap<>();
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

    public TIntObjectHashMap<RdfNode> getNodes() {
        return nodes;
    }

    public RdfNode getNode(int index) {
        return nodes.get(index);
    }

    public TIntObjectHashMap<RdfEdge> getEdges() {
        return edges;
    }

    public RdfEdge getEdge(int index) {
        return edges.get(index);
    }
}
