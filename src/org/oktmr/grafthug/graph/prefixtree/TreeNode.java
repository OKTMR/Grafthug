package org.oktmr.grafthug.graph.prefixtree;

import org.oktmr.grafthug.graph.Edge;

import java.util.HashMap;
import java.util.List;

public class TreeNode {
    private final HashMap<Integer, TreeEdge> prefixTree = new HashMap<>(); //yolo
    private final HashMap<Integer, TreeEdge> edges = new HashMap<>();
    private int id; //index


    public TreeNode(int id) {
        this.id = id;
    }

    public void add(TreeNode treeNode, List<Edge> edges) {
        edges.sort(null);
        if (prefixTree.containsKey(edges.get(0).getId())) {
            // if the prefix tree contains the first edge of the arraylist
            // then we need to complete it :D
            TreeEdge treeEdge = prefixTree.get(edges.get(0).getId());
            for (Edge edge : edges) {
                if (edge.equals(treeEdge)) { // if they are the same :D
                    // we add the treeNode to the edge
                    treeEdge.add(treeNode);
                } else if (treeEdge.getId() > edge.getId()) {
                    // if the edge is greater than the actual one, we need  to insert the actual one :D
                    TreeEdge toInsertEdge = new TreeEdge(edge.getId());
                    toInsertEdge.add(treeNode);

                    toInsertEdge.setLesser(treeEdge.getLesser());
                    toInsertEdge.getLesser().setGreater(toInsertEdge);

                    toInsertEdge.setGreater(treeEdge);
                    toInsertEdge.getGreater().setLesser(toInsertEdge);

                    // TODO : horizontal insert
                }
                // if the edge is less than the actual one :D, we go to the next one :D


                if (!treeEdge.hasGreater()) { // if there is no greater one :)
                    // we create the greater one :)
                    TreeEdge toInsertEdge = new TreeEdge(edge.getId());
                    toInsertEdge.add(treeNode);
                    treeEdge.setGreater(toInsertEdge);
                    toInsertEdge.setLesser(treeEdge);
                }

                treeEdge = treeEdge.getGreater();
            }
        }
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
        if (!(obj instanceof TreeNode)) return false;

        TreeNode n = (TreeNode) obj;

        return this == n || id == n.id;
    }
}
