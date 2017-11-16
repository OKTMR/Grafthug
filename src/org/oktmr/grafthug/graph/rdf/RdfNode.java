package org.oktmr.grafthug.graph.rdf;

import org.oktmr.grafthug.graph.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

public class RdfNode extends Node {
    //public HashMap<RdfEdge, HashSet<RdfNode>> linkIn; //edge, nodeIn = object
    public HashMap<RdfEdge, HashSet<RdfNode>> linkOut; //edge, nodeOut = subject
    public HashMap<Integer, ArrayList<Integer>> indexStructure = new HashMap<>(); //yolo

    RdfNode(int id) {
        super(id);
        //linkIn = new HashMap<>();
        linkOut = new HashMap<>();
    }

    /**
     * this is a object
     * rdfNodeOut is a subject
     */
    public void addLinkOut(RdfEdge edge, RdfNode rdfNodeOut) {
        linkOut.computeIfAbsent(edge, k -> new HashSet<>()).add(rdfNodeOut);
    }

    /**
     * this is a subject
     *
     * @param edge      is edge between the treeNodes
     * @param rdfNodeIn is a object
     */
    public void addLinkIn(RdfEdge edge, RdfNode rdfNodeIn) {
        //linkIn.computeIfAbsent(edge, k -> new HashSet<>()).add(rdfNodeIn);
    }

    /**
     * Create an indexstructure from linkOut
     *
     * @return indexed graph of node
     */
    public void createIndex() {
        for (Entry<RdfEdge, HashSet<RdfNode>> entry : linkOut.entrySet()) {
            for (RdfNode rdfNode : entry.getValue()) {
                indexStructure.computeIfAbsent(rdfNode.getId(), k -> new ArrayList<>()).add(entry.getKey().getId());
            }
        }
        indexStructure.values().forEach(list -> list.sort(null));
    }

    public void clear() {
        //linkIn = null;
        linkOut = null;
        indexStructure = null;
    }
}
