package org.oktmr.grafthug.graph.prefixtree;

import org.oktmr.grafthug.graph.rdf.RdfEdge;
import org.oktmr.grafthug.graph.rdf.RdfNode;

import java.util.*;

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

    /**
     * @param node  object node
     * @param edges sorted list
     * @return the results found for this iteration
     */
    public HashSet<TreeNode> findNeighborhood(Integer node, ArrayList<Integer> edges) {
        HashSet<TreeNode> result = new HashSet<>();

        if (!treeNodes.containsKey(node)) {// there is no object named this way.
            return null;
        }

        TreeNode treeNode = treeNodes.get(node);

        for (Integer edge : edges) { // all the edges are present in the treenode
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
    public HashSet<TreeNode> crawlUp(TreeEdge treeEdge, ListIterator<Integer> edges) {

        HashSet<TreeNode> nodes = new HashSet<>(treeEdge.getNodes()); // it's surely a part of the solution

        if (!edges.hasPrevious()) { // first basic case, there is only one edge
            return nodes;
        }

        // weee gooo uuuuup
        TreeEdge parent = treeEdge.previous();
        int edgeId = edges.previous();

        while (edges.hasPrevious()) {
            if (parent.getId() == edgeId) {// it's the same, the two of them go back
                nodes.retainAll(parent.getNodes()); // we combine the nodes

                /*  this case should never be possible
                if (!treeEdge.hasPrevious()) {
                // there is many edges but the tree stops so not a part of the solution
                    return null;
                }*/

                edgeId = edges.previous();
            } else if (parent.getId() < edgeId) {// missing an edge, there is no solution here
                return null;
            }

            parent = parent.previous(); // we continue to iterate over the parents
        }

        return nodes;
    }


    public boolean findNeighborhood(Integer node, ArrayList<Integer> edges, HashSet<TreeNode> results) {
        if (!treeNodes.containsKey(node)) {// there is no object named this way.
            return false;
        }

        TreeNode treeNode = treeNodes.get(node);

        for (Integer edge : edges) { // all the edges are present in the treenode
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
            HashSet<TreeNode> toMerge = crawlUp(treeEdge, edges.listIterator(edges.size() - 1));
            if (toMerge != null) {
                results.retainAll(toMerge);
            }
        }

        return true;
    }

    public HashSet<TreeNode> evaluate(QueryGraph query) {
        Iterator<Map.Entry<Integer, ArrayList<Integer>>> iterator = query.iterator();

        if (iterator.hasNext()) {
            Map.Entry<Integer, ArrayList<Integer>> entry = iterator.next();

            // we get the results of the first query
            HashSet<TreeNode> results = findNeighborhood(entry.getKey(), entry.getValue());

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
