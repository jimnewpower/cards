package dev.newpower.cards.sim;

import dev.newpower.cards.games.cribbage.CribbageHand;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.moment.Mean;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CribbageSimulator {
    private static final int KNOWN_MIN = 0;
    private static final int KNOWN_MAX = 29;
    private static final int[] IMPOSSIBLE_SCORES = new int[] { 19, 25, 26, 27 };

    private Stats stats;
    private TimeTracker timeTracker;
    private DescriptiveStatistics statistics;
    private Mode mode;

    public CribbageSimulator() {
    }

    public void run(long nSimulations, Random random) {
        timeTracker = new TimeTracker();
        timeTracker.start();

        stats = new Stats();
        statistics = new DescriptiveStatistics();
        mode = new Mode(KNOWN_MIN, KNOWN_MAX, IMPOSSIBLE_SCORES);
        long scoreTotal = 0L;

        for (int i = 0; i < nSimulations; i++) {
            CribbageHand hand = CribbageHand.dealRandom(random);
            int score = hand.scoreHand();

            stats.suggestMin(score);
            stats.suggestMax(score);
            mode.add(score);
            stats.incrementCount();
            statistics.addValue(score);

            scoreTotal += score;
        }

        double mean = scoreTotal / (double) stats.getCount();
        stats.setMean(mean);
        timeTracker.stop();
    }

    public void printStats() {
        String elapsedTime = timeTracker.getElapsedTimeFormatted();
        int modeValue = mode.computeMode();
        NumberFormat nf = NumberFormat.getInstance();
        System.out.println("Cribbage Simulation:");
        System.out.printf("Elapsed time      : %s%n", elapsedTime);
        System.out.printf("Number of hands   : %s%n", nf.format(statistics.getN()));
        System.out.printf("Min score         : %.0f%n", statistics.getMin());
        System.out.printf("Max score         : %.0f%n", statistics.getMax());
        System.out.printf("Mean score        : %.2f%n", statistics.getMean());
        System.out.printf("Median            : %.0f%n", statistics.getPercentile(50));
        System.out.printf("Mode              : %d (%d)%n", modeValue, mode.getCount(modeValue));
        System.out.printf("75th percentile   : %.0f%n", statistics.getPercentile(75));
        System.out.printf("80th percentile   : %.0f%n", statistics.getPercentile(80));
        System.out.printf("85th percentile   : %.0f%n", statistics.getPercentile(85));
        System.out.printf("90th percentile   : %.0f%n", statistics.getPercentile(90));
        System.out.printf("95th percentile   : %.0f%n", statistics.getPercentile(95));
        System.out.printf("99th percentile   : %.0f%n", statistics.getPercentile(99));
        System.out.printf("99.9th percentile : %.0f%n", statistics.getPercentile(99.9));
        System.out.printf("99.99th percentile: %.0f%n", statistics.getPercentile(99.99));
        System.out.printf("Standard Deviation: %.2f%n", statistics.getStandardDeviation());
        System.out.printf("Variance          : %.2f%n", statistics.getVariance());
        System.out.printf("Kurtosis          : %.2f%n", statistics.getKurtosis());
        System.out.printf("Excess Kurtosis   : %.2f%n", statistics.getKurtosis() - 3);
        System.out.printf("Skewness          : %.2f%n", statistics.getSkewness());

        mode.printHistogram();
    }

}
