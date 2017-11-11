package org.oktmr.grafthug.graph.transition;

import org.oktmr.grafthug.graph.Node;
import org.oktmr.grafthug.graph.rdf.RdfEdge;

import java.util.ArrayList;
import java.util.HashMap;

public class TransitionNode extends Node {
    private HashMap<TransitionNode, ArrayList<RdfEdge>> indexes; //yolo

    TransitionNode(int id) {
        super(id);
        indexes = new HashMap<>();
    }

    /**
     * @return id of TreeNode
     */
    public int getId() {
        return id;
    }

    /**
     * @return indexed graph of node
     */
    public HashMap<TransitionNode, ArrayList<RdfEdge>> getIndexStructure() {
        return indexes;
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
        if (!(obj instanceof TransitionNode)) return false;

        TransitionNode n = (TransitionNode) obj;

        return this == n || id == n.id;
    }
}
