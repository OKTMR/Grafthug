package org.oktmr.grafthug.graph.prefixtree;

import java.util.ArrayList;

public class WeightedCondition implements Comparable<WeightedCondition> {
    private int id;
    private ArrayList<Integer> edges;
    private int weight;

    WeightedCondition(int objectId) {
        this.id = objectId;
        edges = new ArrayList<>();
        weight = 0;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public void add(int edge) {
        edges.add(edge);
    }

    public ArrayList<Integer> getEdges() {
        return edges;
    }

    public void sort() {
        edges.sort(null);
    }

    @Override
    public int compareTo(WeightedCondition o) {
        return weight - o.weight;
    }

    public Integer getLast() {
        return edges.get(edges.size() - 1);
    }

    @Override
    public String toString() {
        return id + "=" + edges;
    }
}
