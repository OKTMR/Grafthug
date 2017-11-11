package org.oktmr.grafthug.graph.prefixtree;

import org.oktmr.grafthug.graph.Edge;

import java.util.HashSet;

public class TreeEdge extends Edge {
    private HashSet<TreeNode> treeNodes;

    /**
     * TreeEdge less than this one on the same branch
     */
    private TreeEdge lesser = null;

    /**
     * TreeEdge bigger than this one on the same branch
     */
    private TreeEdge greater = null;

    /**
     * next TreeEdge object with the same Id
     */
    private TreeEdge next = null;

    /**
     * prevoious TreeEdge with the same Id
     */
    private TreeEdge previous = null;

    TreeEdge(int id) {
        super(id);
        this.treeNodes = new HashSet<>();
    }

    /**
     * @return true if has a next element
     */
    public boolean hasNext() {
        return next != null;
    }

    /**
     * @return true if has a greater edge
     */
    public boolean hasGreater() {
        return greater != null;
    }

    public void add(TreeNode treeNode) {
        treeNodes.add(treeNode);
    }

    public TreeEdge getNext() {
        return next;
    }

    public void setNext(TreeEdge next) {
        this.next = next;
    }

    public TreeEdge getPrevious() {
        return previous;
    }

    public void setPrevious(TreeEdge previous) {
        this.previous = previous;
    }

    public TreeEdge getLesser() {
        return lesser;
    }

    public void setLesser(TreeEdge lesser) {
        this.lesser = lesser;
    }

    public TreeEdge getGreater() {
        return greater;
    }

    public void setGreater(TreeEdge greater) {
        this.greater = greater;
    }


}
