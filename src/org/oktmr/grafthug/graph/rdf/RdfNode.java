package org.oktmr.grafthug.graph.rdf;

import org.oktmr.grafthug.graph.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

public class RdfNode extends Node {
    public HashMap<RdfEdge, HashSet<RdfNode>> linkIn; //edge, nodeIn = object
    public HashMap<RdfEdge, HashSet<RdfNode>> linkOut; //edge, nodeOut = subject
    public HashMap<RdfNode, ArrayList<RdfEdge>> indexStructure = new HashMap<>(); //yolo

    RdfNode(int id) {
        super(id);
        linkIn = new HashMap<>();
        linkOut = new HashMap<>();
    }

    /**
     * this is a object
     * rdfNodeOut is a subject
     */
    public void addLinkOut(RdfEdge edge, RdfNode rdfNodeOut) {
        linkOut.computeIfAbsent(edge, k -> new HashSet<RdfNode>()).add(rdfNodeOut);
    }

    /**
     * this is a subject
     *
     * @param edge      is edge between the treeNodes
     * @param rdfNodeIn is a object
     */
    public void addLinkIn(RdfEdge edge, RdfNode rdfNodeIn) {
        linkIn.computeIfAbsent(edge, k -> new HashSet<RdfNode>()).add(rdfNodeIn);
    }

    /**
     * Create an indexstructure from linkOut
     *
     * @return indexed graph of node
     */
    public void createIndex() {
        for (Entry<RdfEdge, HashSet<RdfNode>> entry : linkOut.entrySet()) {
            for (RdfNode rdfNode : entry.getValue()) {
                indexStructure.computeIfAbsent(rdfNode, k -> new ArrayList<>()).add(entry.getKey());
            }
        }
        indexStructure.values().forEach(list -> list.sort(null));
    }

    /**
     * @return indexed graph of node
     */
    public HashMap<RdfNode, ArrayList<RdfEdge>> getIndexStructure() {
        return indexStructure;
    }

    /**
     * @param predicate TreeEdge
     * @return Set of treeNodes
     */
    public ArrayList<RdfNode> requestIndexStructure(RdfEdge predicate) {
        ArrayList<RdfNode> results = new ArrayList<>();
        for (Entry<RdfNode, ArrayList<RdfEdge>> value : indexStructure.entrySet()) {
            for (RdfEdge e : value.getValue()) {
                if (e.equals(predicate)) {
                    results.add(value.getKey());
                }
            }
        }
        return results;
    }
}
