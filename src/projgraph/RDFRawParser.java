package projgraph;

import org.oktmr.grafthug.query.Condition;
import org.oktmr.grafthug.query.Query;
import org.oktmr.grafthug.query.QueryParser;
import org.oktmr.grafthug.query.exception.IncorrectConditionStructure;
import org.oktmr.grafthug.query.exception.IncorrectPrefixStructure;
import org.openrdf.model.Statement;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.Rio;
import org.openrdf.rio.helpers.RDFHandlerBase;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;

public final class RDFRawParser {

    public static final DataStore ds = new DataStore();
    public static final Dictionnaire dico = new Dictionnaire();

    public static void main(String args[]) throws FileNotFoundException {

        Reader reader = new FileReader("University0_0.owl");

        org.openrdf.rio.RDFParser rdfParser = Rio.createParser(RDFFormat.RDFXML);
        rdfParser.setRDFHandler(new RDFListener());
        try {
            rdfParser.parse(reader, "");
        } catch (IOException | RDFParseException | RDFHandlerException e) {
            e.printStackTrace();
        }
        dico.index(); // Generate indexStructure
        System.out.println("Number of nodes : " + dico.nodes.size());
        System.out.println("Number of edges : " + dico.edges.size());

        String q = " PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
                + "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
                + "PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#>" + " SELECT ?x "
                + "WHERE {?x rdf:type ub:Subj18Student .  ?x rdf:type ub:GraduateStudent . ?x rdf:type ub:TeachingAssistant }";
        Query query = null;
        try {
            query = QueryParser.parse(q);
        } catch (IncorrectPrefixStructure | IncorrectConditionStructure e) {
            e.printStackTrace();
        }
        System.out.println(query);
        System.out.println(Arrays.asList(query.getVariables()));
        System.out.println(Arrays.asList(query.getConditions()));
        System.out.println(Arrays.asList(query.getPrefixes()));
        ArrayList<Condition> conds = query.getConditions();

        // fin du pretraitement
        ArrayList<Node> results = new ArrayList<>();
        for (Condition cond : conds) {
            Edge predicate = dico.edges.get(ds.valueIndexes.get(cond.getPredicate().stringValue())); // Return edge
            // (predicate) of a
            // condition
            Node indexNode = dico.nodes.get(ds.valueIndexes.get(cond.getObject().stringValue())); // Return node
            // (object) of a condition
            ArrayList<Node> subjects = indexNode.requestIndexStructure(predicate);

            if (results.isEmpty()) {
                results.addAll(subjects);
            } else {
                results.removeIf(result -> !subjects.contains(result));
            }
        }

        ArrayList<String> finalResults = new ArrayList<>();
        for (Node result : results) {
            finalResults.add(ds.getValue(result.getId()));
        }
        System.out.println("Resultat de la requete : " + finalResults);


        try {
            reader.close();
        } catch (IOException e) {
        }

    }

    private static class RDFListener extends RDFHandlerBase {

        @Override
        public void handleStatement(Statement st) {
            // System.out.println("\n" + st.getSubject() + "\t " + st.getPredicate() + "\t "
            // + st.getObject());
            // Transformer les valeurs en index (int)
            int indexSubject = ds.add(st.getSubject().stringValue());
            int indexPredicate = ds.add(st.getPredicate().stringValue());
            int indexObject = ds.add(st.getObject().stringValue());
            // Ajout des index au dictionnaire
            dico.add(indexSubject, indexPredicate, indexObject);
        }

    }

}
