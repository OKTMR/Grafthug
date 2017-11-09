package projgraph;

public class Edge {
    private int id;

    Edge(int id) {
        this.id = id;
    }

    /**
     * @return pretty print of edgeId
     */
    public String toString() {
        return "e" + id;
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
}
