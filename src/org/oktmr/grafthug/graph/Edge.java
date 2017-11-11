package org.oktmr.grafthug.graph;


public class Edge implements Comparable<Edge> {
    protected int id;

    public Edge(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Edge))
            return false;

        Edge e = (Edge) obj;

        return this == e || this.id == e.id;
    }

    @Override
    public int compareTo(Edge o) {
        return id - o.id;
    }


    /**
     * @return pretty print of edgeId
     */
    public String toString() {
        return "e" + id;
    }

}
