package dev.newpower.cards.sim;

import java.text.NumberFormat;

public class Stats {

    private int min;
    private int max;
    private double mean;
    private int mode;
    private long count;

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public void printStats(String title) {
        System.out.println(title);
        System.out.println("count : " + NumberFormat.getInstance().format(getCount()));
        System.out.println("min   : " + getMin());
        System.out.println("max   : " + getMax());
        System.out.println("mean  : " + getMean());
        System.out.println("mode  : " + getMode());
    }

}
