package org.oktmr.grafthug.graph.prefixtree;

import org.oktmr.grafthug.graph.Node;

import java.util.*;

public class TreeNode extends Node {
    private final HashMap<Integer, TreeEdge> prefixTree; //yolo
    private final HashMap<Integer, LinkedList<TreeEdge>> edges;

    public TreeNode(int id) {
        super(id);
        prefixTree = new HashMap<>();
        edges = new HashMap<>();
    }

    /**
     * Adding from indexStructure
     *
     * @param edges    sorted edges
     * @param treeNode the node to add
     */
    public void add(ArrayList<Integer> edges, int treeNode) {
        // edges are already sorted
        // edges.sort(null);
        if (prefixTree.containsKey(edges.get(0))) {
            // if the prefix tree contains the first edge of the arraylist
            // then we need to complete it :D
            TreeEdge treeEdge = prefixTree.get(edges.get(0));
            updateChain(treeEdge, edges.iterator(), treeNode);
        } else {
            Iterator<Integer> iterator = edges.iterator();

            TreeEdge treeEdge = new TreeEdge(edges.get(0));
            treeEdge.add(treeNode);
            treeEdge.setParent(treeEdge.getId());

            prefixTree.put(treeEdge.getId(), treeEdge);
            this.edges.computeIfAbsent(treeEdge.getId(), k -> new LinkedList<>()).add(treeEdge);

            if (iterator.hasNext()) {
                iterator.next();
                fillChain(treeEdge, iterator, treeNode);
            }
        }
    }

    /**
     * Adds/updates the iterator in the treeEdge
     *
     * @param treeEdge current iterated edge
     * @param iterator iterator over sorted list of edges
     * @param treeNode the node to add to the list
     */
    public void updateChain(TreeEdge treeEdge, Iterator<Integer> iterator, int treeNode) {
        if (iterator.hasNext()) {
            int edge = iterator.next();


            if (edge == treeEdge.getId()) { // if the actual iterator is equal to the actual edge
                treeEdge.add(treeNode);

                if (treeEdge.hasNext()) {// we continue our iteration
                    updateChain(treeEdge.next(), iterator, treeNode);
                } else { // no next elements, so we need to append them
                    fillChain(treeEdge, iterator, treeNode);
                }
            } else if (edge < treeEdge.getId()) {
                // if the iterator is less than the actual, that means that there isn't any equal edge
                TreeEdge toInsertEdge = new TreeEdge(edge);
                toInsertEdge.add(treeNode);
                toInsertEdge.setParent(treeEdge.getParent());

                toInsertEdge.insertBefore(treeEdge);
                addToEdgeChain(toInsertEdge);

                updateChain(treeEdge, iterator, treeNode);
            } else if (edge > treeEdge.getId()) { // if treeEdge edge is smaller
                if (treeEdge.hasNext()) { // then we go to the next
                    updateChain(treeEdge.next(), iterator, treeNode);
                } else {// there is no next
                    // we create it
                    TreeEdge toInsertEdge = new TreeEdge(edge);
                    toInsertEdge.add(treeNode);
                    toInsertEdge.setParent(treeEdge.getParent());

                    toInsertEdge.insertAfter(treeEdge);

                    // we add it to edges
                    addToEdgeChain(toInsertEdge);
                    // we go to the next one
                    fillChain(toInsertEdge, iterator, treeNode);
                }
            }
        }
    }

    /**
     * @param treeEdge last treeEdge (has no Greater)
     * @param iterator array iterator
     * @param treeNode the node to add
     */
    public void fillChain(TreeEdge treeEdge, Iterator<Integer> iterator, int treeNode) {
        if (iterator.hasNext()) {
            // creation
            TreeEdge newChain = new TreeEdge(iterator.next());
            newChain.add(treeNode);
            newChain.setParent(treeEdge.getParent());

            // insertion
            newChain.insertAfter(treeEdge);

            // indexing in edges
            addToEdgeChain(newChain);

            // next one :D
            fillChain(newChain, iterator, treeNode);
        }
    }

    /**
     * Adding to the edge chain
     *
     * @param toInsert edge to insert
     */
    public void addToEdgeChain(TreeEdge toInsert) {
        if (edges.containsKey(toInsert.getId())) {
            LinkedList<TreeEdge> list = edges.get(toInsert.getId());
            ListIterator<TreeEdge> iterator = list.listIterator();

            while (iterator.hasNext()) {
                TreeEdge iterated = iterator.next();
                if (iterated.getParent() > toInsert.getParent()) {
                    iterator.previous();
                    iterator.add(toInsert);
                    return;
                }
            }

            list.add(toInsert);
        } else {
            edges.computeIfAbsent(toInsert.getId(), k -> new LinkedList<>()).add(toInsert);
        }
    }

    public HashMap<Integer, TreeEdge> getPrefixTree() {
        return prefixTree;
    }

    public HashMap<Integer, LinkedList<TreeEdge>> getEdges() {
        return edges;
    }

    /**
     * @return pretty print of nodeId
     */
    public String toString() {
        return "{" + super.toString() + ", e=" + edges.values() + ", p=" + prefixTree.values() + "}";
    }

    public LinkedList<TreeEdge> getEdge(int lastEdge) {
        return edges.get(lastEdge);
    }
}
