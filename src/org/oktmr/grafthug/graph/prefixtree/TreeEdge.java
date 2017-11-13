package org.oktmr.grafthug.graph.prefixtree;

import org.oktmr.grafthug.graph.Edge;

import java.util.HashSet;

public class TreeEdge extends Edge {
    private HashSet<TreeNode> treeNodes;

    private int parent;

    /**
     * TreeEdge less than this one on the same branch
     */
    private TreeEdge previous = null;

    /**
     * TreeEdge bigger than this one on the same branch
     */
    private TreeEdge next = null;

    TreeEdge(int id) {
        super(id);
        this.treeNodes = new HashSet<>();
    }


    /**
     * Insert the actual object as the previous one
     *
     * @param treeEdge next Edge than the actual one
     */
    public void insertBefore(TreeEdge treeEdge) {
        setPrevious(treeEdge.previous());
        setNext(treeEdge);

        previous().setNext(this);
        next().setPrevious(this);
    }

    /**
     * Insert the actual object as the next one
     *
     * @param treeEdge previous Edge than the actual one
     */
    public void insertAfter(TreeEdge treeEdge) {
        if (treeEdge.hasNext()) {
            setNext(treeEdge.next());
            next().setPrevious(this);
        }

        setPrevious(treeEdge);
        previous().setNext(this);
    }

    /**
     * @return true if has a next edge
     */
    public boolean hasNext() {
        return next != null;
    }

    public void add(TreeNode treeNode) {
        treeNodes.add(treeNode);
    }

    public TreeEdge previous() {
        return previous;
    }

    public void setPrevious(TreeEdge previous) {
        this.previous = previous;
    }

    public TreeEdge next() {
        return next;
    }

    public void setNext(TreeEdge next) {
        this.next = next;
    }

    int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return super.toString() + ":" + treeNodes.size();
    }

    public HashSet<TreeNode> getNodes() {
        return treeNodes;
    }

    public boolean hasPrevious() {
        return previous != null;
    }
}
