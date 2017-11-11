package org.oktmr.grafthug.graph;

public class Node {
    protected int id; //index

    public Node(int id) {
        this.id = id;
    }

    /**
     * @return id of TreeNode
     */
    public int getId() {
        return id;
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
        if (!(obj instanceof Node)) return false;

        Node n = (Node) obj;

        return this == n || id == n.id;
    }
}
