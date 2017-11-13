package org.oktmr.grafthug;

import org.oktmr.grafthug.time.Chronos;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 */
public class Log {

    private final BufferedWriter fileWriter;
    private String separator = ";";
    private boolean debug = false;

    public Log(String filePath) throws IOException {
        this.fileWriter = new BufferedWriter(new FileWriter(filePath));
        fileWriter.write("sep=;");
        fileWriter.newLine();
    }

    public Log(String filePath, String separator) throws IOException {
        this.fileWriter = new BufferedWriter(new FileWriter(filePath));
        this.separator = separator;
    }


    public void log(String... string) throws IOException {
        if (debug) System.out.println(String.join(":\t", string));
        fileWriter.write(String.join(separator, string));
        fileWriter.newLine();
    }

    public void log(Chronos chronos) throws IOException {
        if (debug) System.out.println(chronos.toString());
        fileWriter.write(chronos.toCSV());
        fileWriter.newLine();
    }

    public void log(String request, ArrayList<String> list) throws IOException {
        if (debug) System.out.println(request + ":\t" + String.join("\t", list));
        fileWriter.write(request + separator + String.join(separator, list));
        fileWriter.newLine();
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
