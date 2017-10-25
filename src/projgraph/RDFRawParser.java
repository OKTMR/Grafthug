package projgraph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.openrdf.model.Statement;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.Rio;
import org.openrdf.rio.helpers.RDFHandlerBase;

public final class RDFRawParser {

	public static final DataStore ds = new DataStore();
	public static final Dictionnaire dico = new Dictionnaire();

	private static class RDFListener extends RDFHandlerBase {

		@Override
		public void handleStatement(Statement st) {
			System.out.println("\n" + st.getSubject() + "\t " + st.getPredicate() + "\t " + st.getObject());
			int indexSubject = ds.add(st.getSubject().stringValue());
			int indexPredicate = ds.add(st.getPredicate().stringValue());
			int indexObject = ds.add(st.getObject().stringValue());
			//	Value
			dico.add(indexSubject, indexPredicate, indexObject);
		}

	};

	public static void main(String args[]) throws FileNotFoundException {

		Reader reader = new FileReader("University0_0.owl");

		org.openrdf.rio.RDFParser rdfParser = Rio.createParser(RDFFormat.RDFXML);
		rdfParser.setRDFHandler(new RDFListener());
		try {
			rdfParser.parse(reader, "");
		} catch (Exception e) {

		}

		try {
			reader.close();
		} catch (IOException e) {
		}

	}

}
