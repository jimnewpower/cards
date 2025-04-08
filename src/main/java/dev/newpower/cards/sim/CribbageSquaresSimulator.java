package dev.newpower.cards.sim;

import dev.newpower.cards.games.cribbage.CribbageHand;
import dev.newpower.cards.games.cribbage.squares.CribbageSquaresHand;
import dev.newpower.cards.model.Card;
import dev.newpower.cards.model.Deck;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.sql.Time;
import java.text.NumberFormat;
import java.util.*;

public class CribbageSquaresSimulator {
    private static final int KNOWN_MIN = 0;
    private static final int KNOWN_MAX = 170;
    // because we loop on all possible starter cards for a given 16-card grid, divide by 36 to get the target number of simulations
    private static final int DIVISOR = 36;

    private TimeTracker timeTracker;

    private Stats stats;

    private Mode mode;

    private CribbageSquaresHand lowestHand;
    private CribbageSquaresHand highestHand;

    public void run(long nSimulations, Random random) {
        timeTracker = new TimeTracker();
        timeTracker.start();

        mode = new Mode(KNOWN_MIN, KNOWN_MAX);

        stats = new Stats();
        stats.setCount(0L);
        stats.setMin(1000);
        stats.setMax(0);

        long cumulativeScore = 0L;

        Deck deck = new Deck(random);
        final long nSim = computeNSimulations(nSimulations);
        for (long sim = 0L; sim < nSim; sim++) {
            Deque<Card> cards = deck.shuffleSimple();
            int score = simulate(cards);
            cumulativeScore += score;
        }

        double mean = cumulativeScore / (double) stats.getCount();
        stats.setMean(mean);
        timeTracker.stop();
    }

    public void runStacked(long nSimulations, Random random, List<Card> stackedCards) {
        timeTracker = new TimeTracker();
        timeTracker.start();

        mode = new Mode(KNOWN_MIN, KNOWN_MAX);

        stats = new Stats();
        stats.setCount(0L);
        stats.setMin(1000);
        stats.setMax(0);

        long cumulativeScore = 0L;

        final long nSim = computeNSimulations(nSimulations);
        for (long sim = 0L; sim < nSim; sim++) {
            Collections.shuffle(stackedCards);
            Deque<Card> stacked = new LinkedList<>(stackedCards);
            cumulativeScore += simulate(stacked);
        }

        double mean = cumulativeScore / (double) stats.getCount();
        stats.setMean(mean);

        timeTracker.stop();
    }

    private long computeNSimulations(long nSimulations) {
        return (nSimulations + DIVISOR) / DIVISOR;
    }

    private int simulate(Deque<Card> cards) {
        int cumulativeScore = 0;
        CribbageSquaresHand hand = new CribbageSquaresHand(cards);
        Card starter = hand.getStarter();
        while (starter != null) {
            int totalScore = hand.computeScore();

            mode.add(totalScore);
            cumulativeScore += totalScore;
            stats.incrementCount();

            if (totalScore < stats.getMin()) {
                lowestHand = new CribbageSquaresHand(hand);
            }
            if (totalScore > stats.getMax()) {
                highestHand = new CribbageSquaresHand(hand);
            }

            stats.suggestMin(totalScore);
            stats.suggestMax(totalScore);

            starter = cards.poll();
            if (starter != null) {
                hand.setStarter(starter);
            }
        }

        return cumulativeScore;
    }

    public CribbageSquaresHand getLowestHand() {
        return lowestHand;
    }

    public CribbageSquaresHand getHighestHand() {
        return highestHand;
    }

    public String getLowestHandString() {
        return lowestHand == null ? "" : lowestHand.generateHandString();
    }

    public String getHighestHandString() {
        return highestHand == null ? "" : highestHand.generateHandString();
    }

    public void printStats(String title) {
        System.out.println(title + " elapsed time: " + timeTracker.getElapsedTimeFormatted());
    }

    public void printStats() {
        String elapsedTime = timeTracker.getElapsedTimeFormatted();
        int modeValue = mode.computeMode();
        NumberFormat nf = NumberFormat.getInstance();
        System.out.println("Cribbage Squares Simulation:");
        System.out.printf("Elapsed time       : %s%n", elapsedTime);
        System.out.printf("Number of hands    : %s%n", nf.format(mode.getTotalHands()));
        System.out.printf("Min score          : %3d%n", mode.getMin());
        System.out.printf("Max score          : %3d%n", mode.getMax());
        System.out.printf("Mean score         : %.2f%n", mode.getMean());
        System.out.printf("Median             : %.0f%n", mode.getPercentile(50));
        System.out.printf("Mode               : %d (%d)%n", modeValue, mode.getCount(modeValue));
        System.out.printf("60th percentile    : %.0f%n", mode.getPercentile(60));
        System.out.printf("70th percentile    : %.0f%n", mode.getPercentile(70));
        System.out.printf("75th percentile    : %.0f%n", mode.getPercentile(75));
        System.out.printf("80th percentile    : %.0f%n", mode.getPercentile(80));
        System.out.printf("85th percentile    : %.0f%n", mode.getPercentile(85));
        System.out.printf("90th percentile    : %.0f%n", mode.getPercentile(90));
        System.out.printf("91st percentile    : %.0f%n", mode.getPercentile(91));
        System.out.printf("92nd percentile    : %.0f%n", mode.getPercentile(92));
        System.out.printf("93rd percentile    : %.0f%n", mode.getPercentile(93));
        System.out.printf("94th percentile    : %.0f%n", mode.getPercentile(94));
        System.out.printf("95th percentile    : %.0f%n", mode.getPercentile(95));
        System.out.printf("96th percentile    : %.0f%n", mode.getPercentile(96));
        System.out.printf("97th percentile    : %.0f%n", mode.getPercentile(97));
        System.out.printf("98th percentile    : %.0f%n", mode.getPercentile(98));
        System.out.printf("99th percentile    : %.0f%n", mode.getPercentile(99));
        System.out.printf("99.9th percentile  : %.0f%n", mode.getPercentile(99.9));
        System.out.printf("99.99th percentile : %.0f%n", mode.getPercentile(99.99));
        System.out.printf("99.999th percentile: %.0f%n", mode.getPercentile(99.999));
        System.out.printf("Standard Deviation : %.2f%n", mode.getStandardDeviation());
        System.out.printf("Variance           : %.2f%n", mode.getVariance());
//        System.out.printf("Kurtosis           : %.2f%n", mode.getKurtosis());
//        System.out.printf("Excess Kurtosis    : %.2f%n", mode.getKurtosis() - 3);
//        System.out.printf("Skewness           : %.2f%n", mode.getSkewness());

        mode.printHistogram();
    }

}
