package org.oktmr.grafthug.graph.prefixtree;

import org.oktmr.grafthug.graph.Edge;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TreeNode {
    private final HashMap<Integer, TreeEdge> prefixTree = new HashMap<>(); //yolo
    private final HashMap<Integer, LinkedList<TreeEdge>> edges = new HashMap<>();
    private int id; //index


    public TreeNode(int id) {
        this.id = id;
    }

    /**
     * Adding from indexStructure
     *
     * @param treeNode the node to add
     * @param edges    sorted edges
     */
    public void add(TreeNode treeNode, List<Edge> edges) {
        edges.sort(null);
        if (prefixTree.containsKey(edges.get(0).getId())) {
            // if the prefix tree contains the first edge of the arraylist
            // then we need to complete it :D
            TreeEdge treeEdge = prefixTree.get(edges.get(0).getId());
            updateChain(treeEdge, edges.iterator(), treeNode);


        }
    }

    /**
     * @param treeEdge
     * @param iterator
     * @param treeNode
     */
    public void updateChain(TreeEdge treeEdge, Iterator<Edge> iterator, TreeNode treeNode) {
        if (iterator.hasNext()) {
            Edge edge = iterator.next();


            if (treeEdge.equals(edge)) { // if the actual iterator is equal to the actual edge
                treeEdge.add(treeNode);
            } else if (treeEdge.getId() > edge.getId()) {
                // if the iterator is less than the actual, that means that there isn't any equal edge
                TreeEdge toInsertEdge = new TreeEdge(edge.getId());
                toInsertEdge.add(treeNode);

                toInsertEdge.insertBefore(treeEdge);
                edges.computeIfAbsent(toInsertEdge.getId(), k -> new LinkedList<>()).add(toInsertEdge);

                // we continue our iteration
                if (treeEdge.hasGreater()) {
                    updateChain(treeEdge.getGreater(), iterator, treeNode);
                } else {
                    // we can't continue our iteration, so we check if the iterator can continue
                    if (iterator.hasNext()) {
                        // if we can continue, we need to add him to our chain
                        TreeEdge nextEdge = new TreeEdge(iterator.next().getId());
                        nextEdge.add(treeNode);

                        treeEdge.setGreater(nextEdge);
                        nextEdge.setLesser(treeEdge);
                        edges.computeIfAbsent(toInsertEdge.getId(), k -> new LinkedList<>()).add(toInsertEdge);

                        // and we continue on the hype train :D
                        updateChain(nextEdge, iterator, treeNode);
                    }
                }
            } else if (treeEdge.getId() < edge.getId()) { // if treeEdge edge is smaller
                if (treeEdge.hasGreater()) { // then we go to the next
                    updateChain(treeEdge.getGreater(), iterator, treeNode);
                } else {// there is no next
                    TreeEdge toInsertEdge = new TreeEdge(edge.getId());

                    // we add it to edges
                    edges.computeIfAbsent(toInsertEdge.getId(), k -> new LinkedList<>()).add(toInsertEdge);
                    toInsertEdge.add(treeNode);

                    // we create it
                    treeEdge.setGreater(toInsertEdge);
                    toInsertEdge.setLesser(treeEdge);


                    // we go to the next one
                    updateChain(toInsertEdge, iterator, treeNode);
                }
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
