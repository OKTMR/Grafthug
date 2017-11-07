package org.oktmr.grafthug.query;

import java.util.ArrayList;
import java.util.Arrays;

import org.oktmr.grafthug.query.exception.IncorrectConditionStructure;
import org.oktmr.grafthug.query.exception.IncorrectPrefixStructure;

class QueryTest {
    public static void main(String[] args) {
        String q = " PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
                + "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
                + "PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#>"
                + " SELECT ?x "
                + "WHERE {?x rdf:type ub:Subj18Student .  ?x rdf:type ub:GraduateStudent . ?x rdf:type ub:TeachingAssistant }";

        try {
        	Query query = QueryParser.parse(q);
            System.out.println(query);
            System.out.println((query.getVariables()));
            System.out.println((query.getConditions()));
            System.out.println((query.getPrefixes()));
            ArrayList<Condition> conds = query.getConditions();
            for(Condition cond : conds) {
            	int idVar = 1;
            }
            System.out.println(conds);

        } catch (IncorrectPrefixStructure | IncorrectConditionStructure e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
