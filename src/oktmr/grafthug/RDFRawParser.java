package oktmr.grafthug;

import org.openrdf.model.Statement;
import org.openrdf.rio.*;
import org.openrdf.rio.helpers.RDFHandlerBase;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import oktmr.grafthug.query.QueryParser;

public final class RDFRawParser {

  public static final DataStoreManager dataStore = new DataStoreManager();

  public static void main(String args[]) throws IOException, RDFParseException, RDFHandlerException {

    String q = " PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
        + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
        + "PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#>" + " SELECT ?x "
        + "WHERE {?x rdf:type ub:Subj18Student .  ?x rdf:type ub:GraduateStudent . ?x rdf:type ub:TeachingAssistant }";

    QueryParser.parse(q);

    Reader reader = new FileReader("University0_0.owl");

    RDFParser rdfParser = Rio.createParser(RDFFormat.RDFXML);
    rdfParser.setRDFHandler(new RDFListener());

    rdfParser.parse(reader, "");

    reader.close();

    System.out.println(dataStore.objects.get(26).first);
    System.out.println(dataStore.objects.get(26).last);

  }

  private static class RDFListener extends RDFHandlerBase {

    @Override
    public void handleStatement(Statement st) {
      System.out.println("\n" + st.getSubject() + "\t " + st.getPredicate().getNamespace()
          + st.getPredicate().getLocalName() + "\t " + st.getObject());
      dataStore.add(st.getSubject(), st.getPredicate(), st.getObject());
    }

  }

}
