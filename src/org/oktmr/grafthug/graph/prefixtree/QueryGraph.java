package org.oktmr.grafthug.graph.prefixtree;

import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.hash.TIntObjectHashMap;
import org.oktmr.grafthug.DataStore;
import org.oktmr.grafthug.query.Condition;
import org.oktmr.grafthug.query.Query;

import java.util.ArrayList;
import java.util.Iterator;


/**
 *
 */
public class QueryGraph {
    private TIntObjectHashMap<WeightedCondition> queryGraph = new TIntObjectHashMap<>();
    private ArrayList<WeightedCondition> orderedByWeight;

    /**
     * @param ds    DI
     * @param query construct the query graph from
     */
    public QueryGraph(final DataStore ds, final Query query) {
        for (Condition cond : query.getConditions()) {
            // Return edge (predicate) of a condition
            int edgeIndex = ds.getIndex(cond.getPredicate().stringValue());
            // Return node (object) of a condition
            int nodeIndex = ds.getIndex(cond.getObject().stringValue());

            if (edgeIndex == -1 || nodeIndex == -1) {
                queryGraph = new TIntObjectHashMap<>();
                break;
            }

            computeIfAbsent(nodeIndex).add(edgeIndex);
        }
    }


    private WeightedCondition computeIfAbsent(int nodeIndex) {
        if (queryGraph.containsKey(nodeIndex)) {
            return queryGraph.get(nodeIndex);
        } else {
            WeightedCondition wc = new WeightedCondition(nodeIndex);
            queryGraph.put(nodeIndex, wc);
            return wc;
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
        for (TIntObjectIterator<WeightedCondition> iterator = queryGraph.iterator(); iterator.hasNext(); ) {
            iterator.advance();
            WeightedCondition wc = iterator.value();
            wc.sort();
            wc.setWeight(manager.getWeight(wc.getId(), wc.getLast()));
            orderedByWeight.add(wc);
        }

        orderedByWeight.sort(null);
        queryGraph = null;
    }
}
