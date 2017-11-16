package org.oktmr.grafthug.graph.prefixtree;

import gnu.trove.iterator.TIntIterator;
import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.TIntHashSet;
import org.oktmr.grafthug.graph.rdf.RdfNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

public class Manager {
    public TIntObjectHashMap<TreeNode> treeNodes;

    public Manager() {
        this.treeNodes = new TIntObjectHashMap<>();
    }

    public static void addIntersect(TIntHashSet insert, TIntHashSet compare1, TIntHashSet compare2) {
        if (compare1.size() <= compare2.size()) {
            for (TIntIterator iterator = compare1.iterator(); iterator.hasNext(); ) {
                int key = iterator.next();
                if (compare2.contains(key)) {
                    insert.add(key);
                }
            }
        } else {
            for (TIntIterator iterator = compare2.iterator(); iterator.hasNext(); ) {
                int key = iterator.next();
                if (compare1.contains(key)) {
                    insert.add(key);
                }
            }
        }
    }

    public void add(RdfNode node) {
        TreeNode treeNode = computeIfAbsent(node.getId());

        for (TIntObjectIterator<TIntArrayList> iterator = node.indexStructure.iterator();
             iterator.hasNext(); ) {
            iterator.advance();
            treeNode.add(iterator.value(), iterator.key());
        }

        treeNode.compact();
        //System.out.println("treeNode = " + treeNode);
    }

    private TreeNode computeIfAbsent(int id) {
        if (treeNodes.containsKey(id)) {
            return treeNodes.get(id);
        } else {
            TreeNode tr = new TreeNode(id);
            treeNodes.put(id, tr);
            return tr;
        }
    }

    /**
     * @param node  object node
     * @param edges sorted list
     * @return the results found for this iteration
     */
    public TIntHashSet findNeighborhood(int node, ArrayList<Integer> edges) {
        TIntHashSet result = new TIntHashSet();

        if (!treeNodes.containsKey(node)) {// there is no object named this way.
            //System.out.println(node + ":noNode");
            return null;
        }

        TreeNode treeNode = treeNodes.get(node);

        for (int edge : edges) { // all the edges are present in the treenode
            if (!treeNode.getEdges().containsKey(edge)) {
                //System.out.println(edge + ":noEdges");
                return null;
            }
        }

        int lastEdge = edges.get(edges.size() - 1);
        int firstEdge = edges.get(0);

        for (TreeEdge treeEdge : treeNode.getEdge(lastEdge)) {
            if (treeEdge.getParent() > firstEdge) break; // the smallest element is not in the tree
            // we have the guarantee that the tree is bigger than the edges
            // because the smallest element is always in the tree
            TIntHashSet crawlUp = crawlUp(treeEdge, edges.listIterator(edges.size() - 1));
            if (result.size() <= crawlUp.size() / 2) {
                result.ensureCapacity(result.size() + crawlUp.size());
            }
            result.addAll(crawlUp);
        }

        return result;
    }

    public TIntHashSet findNeighborhood(int node, ArrayList<Integer> edges, TIntHashSet lastResult) {
        TIntHashSet result = new TIntHashSet();

        if (!treeNodes.containsKey(node)) {// there is no object named this way.
            //System.out.println(node + ":noNodes");
            return null;
        }

        TreeNode treeNode = treeNodes.get(node);

        for (int edge : edges) { // all the edges are present in the treenode
            if (!treeNode.getEdges().containsKey(edge)) {
                //System.out.println(node + ":noEdge");
                return null;
            }
        }

        int lastEdge = edges.get(edges.size() - 1);
        int firstEdge = edges.get(0);

        for (TreeEdge treeEdge : treeNode.getEdge(lastEdge)) {
            if (treeEdge.getParent() > firstEdge) break; // the smallest element is not in the tree
            // we have the guarantee that the tree is bigger than the edges
            // because the smallest element is always in the tree
            addIntersect(result, lastResult, crawlUp(treeEdge, edges.listIterator(edges.size() - 1)));
        }

        return result;
    }

    /**
     * @param treeEdge iterates over this
     * @param edges    to find if it has this in common
     * @return the nodes which are present
     */
    public TIntHashSet crawlUp(TreeEdge treeEdge, ListIterator<Integer> edges) {

        if (!edges.hasPrevious()) { // first basic case, there is only one edge
            return treeEdge.getNodes();
        }
        TIntHashSet nodes = new TIntHashSet(); // it's surely a part of the solution

        // weee gooo uuuuup
        TreeEdge parent = treeEdge.previous();
        int edgeId = edges.previous();

        TIntHashSet lastNodes = treeEdge.getNodes();
        while (true) {
            if (parent.getId() == edgeId) {// it's the same, the two of them go back
                addIntersect(nodes, lastNodes, parent.getNodes()); // we combine the nodes

                /*  this case should never be possible
                if (!treeEdge.hasPrevious()) {
                // there is many edges but the tree stops so not a part of the solution
                    return null;
                }*/
                if (!edges.hasPrevious()) {
                    return nodes;
                }

                edgeId = edges.previous();
            } else if (parent.getId() < edgeId) {// missing an edge, there is no solution here
                return null;
            }

            parent = parent.previous(); // we continue to iterate over the parents
        }

    }

    public TIntHashSet evaluate(QueryGraph query) {
        query.sort(this);
        Iterator<WeightedCondition> iterator = query.iterator();

        if (iterator.hasNext()) {
            WeightedCondition entry = iterator.next();

            // we get the results of the first query
            TIntHashSet results = findNeighborhood(entry.getId(), entry.getEdges());

            if (results == null) {
                return new TIntHashSet(); // <3
            }
            //System.out.println(entry.getId() + "results:" + results.size());


            while (iterator.hasNext()) {
                entry = iterator.next();
                // we filter the initial result table with the rest
                results = findNeighborhood(entry.getId(), entry.getEdges(), results);
                if (results == null || results.size() == 0) {
                    return new TIntHashSet();
                }
                //System.out.println(entry.getId() + "results:" + results.size());
            }

            return results;
        }

        return new TIntHashSet();
    }

    public void setSize(int size) {
        treeNodes = new TIntObjectHashMap<>(size);
    }

    public int getWeight(Integer nodeIndex, Integer edgeIndex) {
        return treeNodes.containsKey(nodeIndex) ? treeNodes.get(nodeIndex).getWeight(edgeIndex) : 0;
    }
}
