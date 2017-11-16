package org.oktmr.grafthug.graph.prefixtree;

import org.oktmr.grafthug.DataStore;
import org.oktmr.grafthug.query.Condition;
import org.oktmr.grafthug.query.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 */
public class QueryGraph {
    private HashMap<Integer, WeightedCondition> queryGraph = new HashMap<>();
    private ArrayList<WeightedCondition> orderedByWeight;

    /**
     * @param ds    DI
     * @param query construct the query graph from
     */
    public QueryGraph(final DataStore ds, final Query query) {
        for (Condition cond : query.getConditions()) {
            // Return edge (predicate) of a condition
            Integer edgeIndex = ds.getIndex(cond.getPredicate().stringValue());
            // Return node (object) of a condition
            Integer nodeIndex = ds.getIndex(cond.getObject().stringValue());

            if (edgeIndex == null || nodeIndex == null) {
                queryGraph = new HashMap<>();
                break;
            }

            queryGraph.computeIfAbsent(nodeIndex, WeightedCondition::new).add(edgeIndex);
        }
    }

    /**
     * Decorator for the hashmap iterator
     *
     * @return queryGraph iterator
     */
    public Iterator<WeightedCondition> iterator() {
        return orderedByWeight.iterator();
    }


    @Override
    public String toString() {
        return String.valueOf(queryGraph);
    }

    public void sort(final Manager manager) {
        orderedByWeight = new ArrayList<>(queryGraph.size());
        for (WeightedCondition wc : queryGraph.values()) {
            wc.sort();
            wc.setWeight(manager.getWeight(wc.getId(), wc.getLast()));
            orderedByWeight.add(wc);
        }

        orderedByWeight.sort(null);
        queryGraph = null;
    }
}
