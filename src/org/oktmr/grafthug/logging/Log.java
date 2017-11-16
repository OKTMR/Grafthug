package org.oktmr.grafthug.logging;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 *
 */
public class Log {

    protected final PrintStream fileWriter;
    protected String separator = ";";
    protected boolean debug = false;

    public Log(String filePath) throws IOException {
        this.fileWriter = new PrintStream(new File(filePath));
        fileWriter.println("sep=;");
    }

    public Log(String filePath, String separator) throws IOException {
        this.fileWriter = new PrintStream(new File(filePath));
        this.separator = separator;
    }

    public Log(PrintStream ps) {
        fileWriter = ps;
    }

    public void log(String... string) throws IOException {
        if (debug) System.out.println(String.join("\t", string));
        fileWriter.println(String.join(separator, string));
    }

    public void log(Chronos chronos) throws IOException {
        if (debug) System.out.println(chronos.toString());
        fileWriter.println(chronos.toCSV());
    }

    public void log(String request, ArrayList<String> list) throws IOException {
        if (debug) System.out.println(request + ":\t" + String.join("\t", list));
        fileWriter.println(request + separator + String.join(separator, list));
    }

    public void flush() throws IOException {
        fileWriter.flush();
    }

    public void debug() {
        debug = true;
    }

    public void close() throws IOException {
        fileWriter.close();
    }
}
