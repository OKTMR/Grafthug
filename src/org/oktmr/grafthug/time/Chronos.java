package org.oktmr.grafthug.time;

/**
 *
 */
public class Chronos {
    private String name;
    private long start;
    private long stop;
    private long step;
    private boolean isStopped;


    private Chronos(String name) {
        this.name = name;
        isStopped = false;
        start = now();
        step = start;
    }

    public static Chronos start(String name) {
        return new Chronos(name);
    }

    private static long now() {
        return System.nanoTime();
    }

    public static double toMillis(long interval) {
        return ((double) interval) / 1000000;
    }

    public static String formatMillis(double value) {
        return String.format("%.6f", value);
    }

    public static String formatNano(long value) {
        return String.format("%.6f", toMillis(value));
    }

    public long stop() {
        stop = now();
        isStopped = true;
        return stop - start;
    }

    public long step() {
        if (isStopped)
            return stop - start;
        else {
            stop = now();
            return stop - start;
        }
    }

    public long duration() {
        return stop - start;
    }

    public long interval() {
        long now = now();
        long returned = now - this.step;
        this.step = now;
        return returned;
    }

    @Override
    public String toString() {
        return name + " time : " + formatNano(step());
    }

    public String toCSV() {
        return name + ";" + formatNano(step());
    }
}
