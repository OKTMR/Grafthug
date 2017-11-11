package org.oktmr.grafthug.graph.transition;

import org.oktmr.grafthug.graph.rdf.RdfEdge;
import org.oktmr.grafthug.graph.rdf.RdfNode;

import java.util.HashMap;

public class Dictionnaire {


    public HashMap<Integer, RdfNode> nodes; //subjects, objects
    public HashMap<Integer, RdfEdge> edges; //predicates
}
