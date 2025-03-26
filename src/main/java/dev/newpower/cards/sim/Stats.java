package dev.newpower.cards.sim;

import java.text.NumberFormat;
import java.util.Map;

public class Stats {
    // TODO: use Atomic values to make this thread-safe and support multi-threaded simulations
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

    public void suggestMin(int suggested) {
        if (suggested < min) {
            min = suggested;
        }
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void suggestMax(int suggested) {
        if (suggested > max) {
            max = suggested;
        }
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

    public void incrementCount() {
        ++count;
    }

    public void computeMode(Map<Integer, Long> modeMap) {
        int mode = -1;
        long most = 0L;
        for (Integer score : modeMap.keySet()) {
            if (modeMap.get(score).longValue() > most) {
                most = modeMap.get(score).longValue();
                mode = score.intValue();
            }
        }
        setMode(mode);
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
