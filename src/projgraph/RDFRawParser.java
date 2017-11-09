package projgraph;

import org.openrdf.model.Statement;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.Rio;
import org.openrdf.rio.helpers.RDFHandlerBase;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public final class RDFRawParser {

    public static final DataStore ds = new DataStore();
    public static final Dictionnaire dico = new Dictionnaire();

    public static void main(String args[]) throws FileNotFoundException {

        long begin = System.currentTimeMillis();
        Reader reader = new FileReader("University0_0.owl");

        org.openrdf.rio.RDFParser rdfParser = Rio
                .createParser(RDFFormat.RDFXML);
        rdfParser.setRDFHandler(new RDFListener());
        try {
            rdfParser.parse(reader, "");
            dico.index(); //Generate indexStructure

            System.out.println(System.currentTimeMillis() - begin);
            System.out.println("Number of nodes : " + dico.nodes.size());
            System.out.println("Number of edges : " + dico.edges.size());
            System.out.println(ds.getValueToIndex(349)); //Just a test

        } catch (Exception e) {

        }

        try {
            reader.close();
        } catch (IOException e) {
        }

    }

    ;

    private static class RDFListener extends RDFHandlerBase {

        @Override
        public void handleStatement(Statement st) {
            //System.out.println("\n" + st.getSubject() + "\t " + st.getPredicate() + "\t " + st.getObject());
            //Transformer les valeurs en index (int)
            int indexSubject = ds.add(st.getSubject().stringValue());
            int indexPredicate = ds.add(st.getPredicate().stringValue());
            int indexObject = ds.add(st.getObject().stringValue());
            //Ajout des index au dictionnaire
            dico.add(indexSubject, indexPredicate, indexObject);
        }

    }

}
