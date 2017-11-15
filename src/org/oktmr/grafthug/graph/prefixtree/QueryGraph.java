package org.oktmr.grafthug.graph.prefixtree;

import org.oktmr.grafthug.DataStore;
import org.oktmr.grafthug.query.Condition;
import org.oktmr.grafthug.query.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 */
public class QueryGraph {
    private HashMap<Integer, ArrayList<Integer>> queryGraph = new HashMap<>();

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

            queryGraph.computeIfAbsent(nodeIndex, k -> new ArrayList<>()).add(edgeIndex);
        }

        queryGraph.forEach((node, edges) -> edges.sort(null));
    }

    /**
     * Decorator for the hashmap iterator
     *
     * @return queryGraph iterator
     */
    public Iterator<Map.Entry<Integer, ArrayList<Integer>>> iterator() {
        return queryGraph.entrySet().iterator();
    }


    @Override
    public String toString() {
        return String.valueOf(queryGraph);
    }
}
