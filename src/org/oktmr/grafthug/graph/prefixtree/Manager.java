package org.oktmr.grafthug.graph.prefixtree;

import org.oktmr.grafthug.graph.rdf.RdfEdge;
import org.oktmr.grafthug.graph.rdf.RdfNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Manager {
    public HashMap<Integer, TreeNode> treeNodes;

    public Manager() {
        this.treeNodes = new HashMap<>();
    }


    public void add(RdfNode node) {
        TreeNode treeNode = treeNodes.computeIfAbsent(node.getId(), TreeNode::new);


        for (Map.Entry<RdfNode, ArrayList<RdfEdge>> entry : node.indexStructure.entrySet()) {
            TreeNode iter = treeNodes.computeIfAbsent(entry.getKey().getId(), TreeNode::new);

            treeNode.add(entry.getValue(), iter);
        }
        // System.out.println("treeNode = " + treeNode);
    }
}
