package org.oktmr.grafthug;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.set.hash.TIntHashSet;
import org.oktmr.grafthug.graph.prefixtree.Manager;
import org.oktmr.grafthug.graph.prefixtree.QueryGraph;
import org.oktmr.grafthug.graph.rdf.Dictionnaire;
import org.oktmr.grafthug.graph.rdf.RdfNode;
import org.oktmr.grafthug.logging.Chronos;
import org.oktmr.grafthug.logging.Log;
import org.oktmr.grafthug.query.Query;
import org.oktmr.grafthug.query.QueryParser;
import org.oktmr.grafthug.query.exception.IncorrectConditionStructure;
import org.oktmr.grafthug.query.exception.IncorrectPrefixStructure;
import org.openrdf.model.Statement;
import org.openrdf.rio.*;
import org.openrdf.rio.helpers.RDFHandlerBase;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

public final class Main {

    private static DataStore ds = new DataStore();
    private static Dictionnaire dico = new Dictionnaire();
    private static Manager manager = new Manager();
    private static long totalParsingTime = 0;
    private static long totalPreProcessTime = 0;
    private static long totalProcessTime = 0;
    private static long total = 0;
    @Parameter(names = {"-v", "--verbose"}, description = "Allows the debug logs to be displayed")
    private boolean debug = false;
    @Parameter(names = {"-o", "--output"}, description = "CSV result output file", arity = 1)
    private String resultPath = "results.csv";
    @Parameter(names = {"-t", "--timer"}, description = "CSV timer result output file")
    private String timerFileOut = "timer.csv";
    @Parameter(names = {"-h", "--help"}, description = "Displays the help and version", help = true)
    private boolean help;
    @Parameter(names = {"-r", "--request"}, description = "Unary Request to execute. Either this or -q is required")
    private String request = null;
    @Parameter(names = {"-i", "--input"}, description = "Input File for Data", required = true)
    private String dataFilePath = null;
    @Parameter(names = {"-q", "--query"}, description = "File that contains requests. Either this or -r is required")
    private String requestFilePath = null;
    private Log timerLog;
    private Log resultLog;

    public static void main(String... args) throws Exception {
        Main main = new Main();
        JCommander jcommander = JCommander.newBuilder().addObject(main).build();
        jcommander.setProgramName(getVersion());
        try {
            jcommander.parse(args);
            main.run(jcommander);
        } catch (ParameterException e) {
            System.out.println(e.getLocalizedMessage());
            System.out.println();
            jcommander.usage();
        }
    }

    private static String getVersion() {
        return "Grafthug v2.0.0 - Alley";
    }

    /**
     * Indexes the given dataset
     *
     * @param dataFilePath data file path
     * @throws IOException         if there is an error while reading the file
     * @throws RDFParseException   if there is an error while parsing the file
     * @throws RDFHandlerException if there is an error while iterating the data file
     */
    private static void indexation(String dataFilePath) throws IOException, RDFParseException, RDFHandlerException {
        Reader reader = new FileReader(dataFilePath);

        RDFParser rdfParser = Rio.createParser(RDFFormat.RDFXML);
        rdfParser.setRDFHandler(new RDFListener());

        rdfParser.parse(reader, "");
        reader.close();

        ds.compact();
        //dico.index();

        manager.setSize(dico.nodes.size());

        TIntObjectIterator<RdfNode> iterator = dico.nodes.iterator();
        for (int i = dico.nodes.size(); i-- > 0; ) {
            iterator.advance();
            RdfNode rdfNode = iterator.value();
            rdfNode.createIndex();
            manager.add(rdfNode);
            rdfNode.clear();
        }

        dico = null;
    }

    private void run(JCommander jcommander) throws IOException, RDFParseException, RDFHandlerException,
            IncorrectPrefixStructure, IncorrectConditionStructure {
        if (help) { // displays the help
            jcommander.usage();
            return;
        }

        if (request == null && requestFilePath == null) {
            throw new ParameterException("The following option is required: [-r | --request | -q | --query]");
        }

        Chronos chronoTotal = Chronos.start("Total");
        // beginning indexation
        Chronos chronoIndex =
                Chronos.start("Indexation");
        indexation(dataFilePath);
        chronoIndex.stop();


        timerLog = new Log(timerFileOut);
        resultLog = new Log(resultPath);

        timerLog.debug();
        if (debug) {
            resultLog.debug();
        }
        ArrayList<QueryGraph> queryGraphs = new ArrayList<>();
        Chronos chronoExec = Chronos.start("Parsing + Pre-process + Evaluation + Logging");
        if (requestFilePath != null) {
            ArrayList<String> stringQueries = parseFile(requestFilePath);
            ArrayList<Query> queries = parseQueries(stringQueries);
            queryGraphs = preProcessQueries(queries);
            exec(queryGraphs);

        } else if (request != null) {
            ArrayList<String> stringQueries = new ArrayList<>(1);
            stringQueries.add(request);
            ArrayList<Query> queries = parseQueries(stringQueries);
            queryGraphs = preProcessQueries(queries);
            exec(queryGraphs);
        }
        chronoExec.stop();
        chronoTotal.stop();

        total += totalParsingTime + totalPreProcessTime + totalProcessTime;
        timerLog.log(chronoIndex);
        timerLog.log("Parsing    : ", Chronos.formatMillis(totalParsingTime));
        timerLog.log("Pre-process: ", Chronos.formatMillis(totalPreProcessTime));
        timerLog.log("Evaluation : ", Chronos.formatMillis(totalProcessTime));
        timerLog.log("Query Count      : ", String.valueOf(queryGraphs.size()));
        timerLog.log(chronoExec);
        timerLog.log(chronoTotal);

    }

    private ArrayList<Query> parseQueries(
            ArrayList<String> stringQueries) throws IncorrectPrefixStructure, IncorrectConditionStructure {
        ArrayList<Query> queries = new ArrayList<>(stringQueries.size());

        Chronos chronoQuery = Chronos.start("Parsing");
        for (String stringQuery : stringQueries) {
            //System.out.println(queryString);
            queries.add(QueryParser.parse(stringQuery));
        }
        totalParsingTime = chronoQuery.stop();

        return queries;
    }

    private ArrayList<QueryGraph> preProcessQueries(ArrayList<Query> queries) {
        ArrayList<QueryGraph> queryGraphs = new ArrayList<>(queries.size());

        Chronos chronoPreprocess = Chronos.start("Pre-process");
        for (Query query : queries) {
            //System.out.println(query);
            queryGraphs.add(new QueryGraph(ds, query));
        }
        totalPreProcessTime = chronoPreprocess.stop();

        return queryGraphs;
    }

    private void exec(ArrayList<QueryGraph> queryGraphs) throws IncorrectPrefixStructure,
            IncorrectConditionStructure,
            IOException {


        //System.out.println(queryGraph);

        Chronos chronoProcess =
                Chronos.start("Process");

        for (int i = 0, queryGraphsSize = queryGraphs.size(); i < queryGraphsSize; i++) {
            QueryGraph queryGraph = queryGraphs.get(i);
            TIntHashSet results = manager.evaluate(queryGraph);

            ArrayList<String> finalResults = new ArrayList<>(results.size());
            for (TIntIterator iterator = results.iterator(); iterator.hasNext(); ) {
                int result = iterator.next();
                finalResults.add(ds.getValue(result));
            }

            resultLog.log("Q" + String.format("% 2d", i + 1), finalResults);
        }


        totalProcessTime = chronoProcess.stop();

    }

    private ArrayList<String> parseFile(String requestFilePath) throws IOException, IncorrectPrefixStructure,
            IncorrectConditionStructure {
        ArrayList<String> stringQueries = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader(requestFilePath));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            line = line.trim();

            if (line.length() > 0) {
                if (line.charAt(0) == '#') {
                    continue;
                }

                sb.append(line).append("\n");

                if (line.charAt(line.length() - 1) == '}') {
                    //exec(iterator, sb.toString());
                    stringQueries.add(sb.toString());
                    sb = new StringBuilder();
                }
            }
        }

        reader.close();

        return stringQueries;
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
