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
        return System.currentTimeMillis();
    }

    public static String formatMillis(long value) {
        return (value < 1) ? "<1 ms" : value + " ms";
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
        if(start == stop){
            return 1;
        }
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
        return name + " time:\t" + formatMillis(step());
    }

    public String toCSV() {
        return name + ";" + formatMillis(step());
    }
}
