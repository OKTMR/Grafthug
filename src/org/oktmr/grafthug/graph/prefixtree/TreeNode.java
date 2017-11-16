package org.oktmr.grafthug.graph.prefixtree;

import gnu.trove.iterator.TIntIterator;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import org.oktmr.grafthug.graph.Node;

import java.util.LinkedList;
import java.util.ListIterator;

public class TreeNode extends Node {
    private TIntObjectHashMap<TreeEdge> prefixTree; //yolo
    private TIntObjectHashMap<LinkedList<TreeEdge>> edges;
    private TIntIntHashMap edgesWeight;

    public TreeNode(int id) {
        super(id);
        prefixTree = new TIntObjectHashMap<>();
        edges = new TIntObjectHashMap<>();
        edgesWeight = new TIntIntHashMap(10, 0.75f);
    }

    /**
     * Adding from indexStructure
     *
     * @param edges    sorted edges
     * @param treeNode the node to add
     */
    public void add(TIntArrayList edges, int treeNode) {
        // edges are already sorted
        // edges.sort(null);

        if (prefixTree.containsKey(edges.get(0))) {
            // if the prefix tree contains the first edge of the arraylist
            // then we need to complete it :D
            TreeEdge treeEdge = prefixTree.get(edges.get(0));
            updateChain(treeEdge, edges.iterator(), treeNode);
        } else {
            TIntIterator iterator = edges.iterator();

            TreeEdge treeEdge = new TreeEdge(edges.get(0));
            treeEdge.add(treeNode);
            treeEdge.setParent(treeEdge.getId());

            prefixTree.put(treeEdge.getId(), treeEdge);
            computeIfAbsent(treeEdge.getId()).add(treeEdge);

            if (iterator.hasNext()) {
                iterator.next();
                fillChain(treeEdge, iterator, treeNode);
            }
        }
    }

    public LinkedList<TreeEdge> computeIfAbsent(int id) {
        if (edges.containsKey(id)) {
            return edges.get(id);
        }
        LinkedList<TreeEdge> ll = new LinkedList<>();
        edges.put(id, ll);
        return ll;
    }

    /**
     * Adds/updates the iterator in the treeEdge
     *
     * @param treeEdge current iterated edge
     * @param iterator iterator over sorted list of edges
     * @param treeNode the node to add to the list
     */
    public void updateChain(TreeEdge treeEdge, TIntIterator iterator, int treeNode) {
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
    public void fillChain(TreeEdge treeEdge, TIntIterator iterator, int treeNode) {
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
            computeIfAbsent(toInsert.getId()).add(toInsert);
        }
    }

    public TIntObjectHashMap<TreeEdge> getPrefixTree() {
        return prefixTree;
    }

    public TIntObjectHashMap<LinkedList<TreeEdge>> getEdges() {
        return edges;
    }

    /**
     * @return pretty print of nodeId
     */
    public String toString() {
        return "{" + super.toString() + ", e=" + edges + ", p=" + prefixTree + "}";
    }

    public LinkedList<TreeEdge> getEdge(int lastEdge) {
        return edges.get(lastEdge);
    }

    public Integer getWeight(Integer edgeIndex) {
        return edges.containsKey(edgeIndex) ? edges.get(edgeIndex).getFirst().getNodes().size() : 0;
    }

    public void compact() {
        edgesWeight.compact();
        edges.compact();
        prefixTree.compact();
    }
}
