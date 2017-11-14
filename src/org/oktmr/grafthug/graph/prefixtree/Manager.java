package org.oktmr.grafthug.graph.prefixtree;

import org.oktmr.grafthug.graph.rdf.RdfNode;

import java.util.*;

public class Manager {
    public HashMap<Integer, TreeNode> treeNodes;

    public Manager() {
        this.treeNodes = new HashMap<>();
    }

    public static HashSet<Integer> retainAll(HashSet<Integer> treeNodes, HashSet<Integer> treeNodes2) {
        if (treeNodes.size() < treeNodes2.size()) {
            treeNodes.retainAll(treeNodes2);
            return treeNodes;
        } else {
            treeNodes2.retainAll(treeNodes);
            return treeNodes2;
        }
    }

    public void add(RdfNode node) {
        TreeNode treeNode = treeNodes.computeIfAbsent(node.getId(), TreeNode::new);

        for (Map.Entry<Integer, ArrayList<Integer>> entry : node.indexStructure.entrySet()) {
            treeNode.add(entry.getValue(), entry.getKey());
        }
        // System.out.println("treeNode = " + treeNode);
    }

    /**
     * @param node  object node
     * @param edges sorted list
     * @return the results found for this iteration
     */
    public HashSet<Integer> findNeighborhood(int node, ArrayList<Integer> edges) {
        HashSet<Integer> result = new HashSet<>();

        if (!treeNodes.containsKey(node)) {// there is no object named this way.
            return null;
        }

        TreeNode treeNode = treeNodes.get(node);

        for (int edge : edges) { // all the edges are present in the treenode
            if (!treeNode.getEdges().containsKey(edge)) {
                return null;
            }
        }

        int lastEdge = edges.get(edges.size() - 1);
        int firstEdge = edges.get(0);

        for (TreeEdge treeEdge : treeNode.getEdge(lastEdge)) {
            if (treeEdge.getParent() > firstEdge) break; // the smallest element is not in the tree
            // we have the guarantee that the tree is bigger than the edges
            // because the smallest element is always in the tree
            result.addAll(crawlUp(treeEdge, edges.listIterator(edges.size() - 1)));
        }

        return result;
    }

    /**
     * @param treeEdge iterates over this
     * @param edges    to find if it has this in common
     * @return the nodes which are present
     */
    public HashSet<Integer> crawlUp(TreeEdge treeEdge, ListIterator<Integer> edges) {

        HashSet<Integer> nodes = new HashSet<>(treeEdge.getNodes()); // it's surely a part of the solution

        if (!edges.hasPrevious()) { // first basic case, there is only one edge
            return nodes;
        }

        // weee gooo uuuuup
        TreeEdge parent = treeEdge.previous();
        int edgeId = edges.previous();

        while (true) {
            if (parent.getId() == edgeId) {// it's the same, the two of them go back
                nodes.retainAll(parent.getNodes()); // we combine the nodes

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

    public boolean findNeighborhood(int node, ArrayList<Integer> edges, HashSet<Integer> results) {
        if (!treeNodes.containsKey(node)) {// there is no object named this way.
            return false;
        }

        TreeNode treeNode = treeNodes.get(node);

        for (int edge : edges) { // all the edges are present in the treenode
            if (!treeNode.getEdges().containsKey(edge)) {
                return false;
            }
        }

        int lastEdge = edges.get(edges.size() - 1);
        int firstEdge = edges.get(0);

        for (TreeEdge treeEdge : treeNode.getEdge(lastEdge)) {
            if (treeEdge.getParent() > firstEdge) break; // the smallest element is not in the tree
            // we have the guarantee that the tree is bigger than the edges
            // because the smallest element is always in the tree
            HashSet<Integer> toMerge = crawlUp(treeEdge, edges.listIterator(edges.size() - 1));
            if (toMerge != null) {
                results = retainAll(results, toMerge);
            }
        }

        return true;
    }

    public HashSet<Integer> evaluate(QueryGraph query) {
        Iterator<Map.Entry<Integer, ArrayList<Integer>>> iterator = query.iterator();

        if (iterator.hasNext()) {
            Map.Entry<Integer, ArrayList<Integer>> entry = iterator.next();

            // we get the results of the first query
            HashSet<Integer> results = findNeighborhood(entry.getKey(), entry.getValue());

            if (results == null) {
                return new HashSet<>(); // <3
            }

            while (iterator.hasNext()) {
                entry = iterator.next();
                // we filter the initial result table with the rest
                if (findNeighborhood(entry.getKey(), entry.getValue(), results))
                    return new HashSet<>();
            }

            return results;
        }

        return new HashSet<>();
    }
}
