package oktmr.grafthug;

import org.openrdf.model.Statement;
import org.openrdf.rio.*;
import org.openrdf.rio.helpers.RDFHandlerBase;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public final class RDFRawParser {

    public static final DataStoreManager dataStore = new DataStoreManager();

    public static void main(
            String args[]) throws IOException, RDFParseException, RDFHandlerException {
        Reader reader = new FileReader("University0_0.owl");

        RDFParser rdfParser = Rio
                .createParser(RDFFormat.RDFXML);
        rdfParser.setRDFHandler(new RDFListener());

        rdfParser.parse(reader, "");

        reader.close();

        System.out.println(dataStore.objects.get(26).first);
        System.out.println(dataStore.objects.get(26).last);

    }

    private static class RDFListener extends RDFHandlerBase {

        @Override
        public void handleStatement(Statement st) {
            System.out.println("\n" + st.getSubject() +
                                       "\t " + st.getPredicate().getNamespace() + st.getPredicate().getLocalName() +
                                       "\t " + st.getObject());
            dataStore.add(st.getSubject(), st.getPredicate(), st.getObject());
        }

    }

}