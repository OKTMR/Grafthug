package org.oktmr.grafthug;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import org.oktmr.grafthug.graph.prefixtree.Manager;
import org.oktmr.grafthug.graph.prefixtree.QueryGraph;
import org.oktmr.grafthug.graph.rdf.Dictionnaire;
import org.oktmr.grafthug.graph.rdf.RdfNode;
import org.oktmr.grafthug.query.Query;
import org.oktmr.grafthug.query.QueryParser;
import org.oktmr.grafthug.query.exception.IncorrectConditionStructure;
import org.oktmr.grafthug.query.exception.IncorrectPrefixStructure;
import org.oktmr.grafthug.time.Chronos;
import org.openrdf.model.Statement;
import org.openrdf.rio.*;
import org.openrdf.rio.helpers.RDFHandlerBase;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;

public final class Main {

    private static final DataStore ds = new DataStore();
    private static final Dictionnaire dico = new Dictionnaire();
    private static final Manager manager = new Manager();

    private static long totalParsingTime = 0;
    private static long totalPreProcessTime = 0;
    private static long totalProcessTime = 0;


    @Parameter(names = {"-d", "--debug"}, description = "Allows the debug logs to be displayed")
    private boolean debug = false;
    @Parameter(names = {"-o", "--output"}, description = "CSV result output file", arity = 1)
    private String resultPath = "results.csv";
    @Parameter(names = {"-t", "--timer"}, description = "CSV timer result output file")
    private String timerFileOut = "timer.csv";
    @Parameter(names = {"-h", "--help"}, description = "Displays the help", help = true)
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
        jcommander.setProgramName("grafthug");
        try {
            jcommander.parse(args);
            main.run(jcommander);
        } catch (ParameterException e) {
            System.out.println(e.getLocalizedMessage());
            System.out.println();
            jcommander.usage();
        }
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

        dico.index();

        for (RdfNode rdfNode : dico.nodes.values()) {
            manager.add(rdfNode);
        }
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


        timerLog = new Log(timerFileOut);
        resultLog = new Log(resultPath);

        timerLog.debug();
        if (debug) {
            resultLog.debug();
        }

        // beginning indexation
        Chronos chronoIndex =
                Chronos.start("Indexation");
        indexation(dataFilePath);
        chronoIndex.stop();
        timerLog.log(chronoIndex);

        System.out.println("Number of treeNodes : " + manager.treeNodes.size());
        System.out.println("Number of edges : " + dico.getEdges().size());

        timerLog.log("Q n°", "Parsing ", "Pre-Process", "Evaluation");

        if (requestFilePath != null) {
            parseFile(requestFilePath);
        } else if (request != null) {
            exec(1, request);
        }

        timerLog.log("Total Parsing", Chronos.formatNano(totalParsingTime));
        timerLog.log("Total Pre-process", Chronos.formatNano(totalPreProcessTime));
        timerLog.log("Total Process", Chronos.formatNano(totalProcessTime));
    }

    private void parseFile(
            String requestFilePath) throws IOException, IncorrectPrefixStructure, IncorrectConditionStructure {
        BufferedReader reader = new BufferedReader(new FileReader(requestFilePath));

        StringBuilder sb = new StringBuilder();
        String line;
        int iterator = 1;
        while ((line = reader.readLine()) != null) {
            line = line.trim();

            if (line.length() > 1) {
                sb.append(line);

                if (line.charAt(line.length() - 1) == '}') {
                    exec(iterator, sb.toString());

                    ++iterator;
                    sb = new StringBuilder();
                }
            }
        }

        reader.close();
    }

    private void exec(int queryNumber, String queryString) throws IncorrectPrefixStructure,
            IncorrectConditionStructure,
            IOException {
        ArrayList<String> times = new ArrayList<>(3);
        Chronos chronoQuery =
                Chronos.start("Parsing");
        Query query = QueryParser.parse(queryString);
        times.add(Chronos.formatNano(chronoQuery.stop()));
        totalParsingTime += chronoQuery.duration();

        Chronos chronoPreprocess =
                Chronos.start("Pre-process");
        QueryGraph queryGraph = new QueryGraph(ds, query);
        times.add(Chronos.formatNano(chronoPreprocess.stop()));
        totalPreProcessTime += chronoPreprocess.duration();

        Chronos chronoProcess =
                Chronos.start("Process");
        HashSet<Integer> results = manager.evaluate(queryGraph);
        times.add(Chronos.formatNano(chronoProcess.stop()));
        totalProcessTime += chronoProcess.duration();


        timerLog.log("Q" + String.format("% 2d", queryNumber), times);
        timerLog.flush();

        ArrayList<String> finalResults = new ArrayList<>(results.size());
        for (int result : results) {
            finalResults.add(ds.getValue(result));
        }
        resultLog.log("Q" + String.format("% 2d", queryNumber), finalResults);
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
