package dev.newpower.cards.sim;

import dev.newpower.cards.games.cribbage.CribbageHand;
import dev.newpower.cards.model.Card;
import dev.newpower.cards.model.Deck;

import java.sql.Time;
import java.text.NumberFormat;
import java.util.*;

public class CribbageSquaresSimulator {
    private TimeTracker timeTracker;

    private Stats stats;

    private Map<Integer, Long> modeMap;

    private String highestHandString;

    public void run(long nSimulations, Random random) {
        timeTracker = new TimeTracker();
        timeTracker.start();

        stats = new Stats();
        stats.setCount(0L);
        stats.setMin(1000);
        stats.setMax(0);

        modeMap = new HashMap<>();

        long cumulativeScore = 0L;

        highestHandString = "";

        for (long sim = 0L; sim < nSimulations; sim++) {
            Deck deck = new Deck(random);
            Deque<Card> cards = deck.shuffle();
            cumulativeScore += simulate(cards, stats);
        }

        double mean = cumulativeScore / (double) stats.getCount();
        stats.setMean(mean);
        stats.computeMode(modeMap);

        timeTracker.stop();
    }

    public void runStacked(long nSimulations, Random random, List<Card> stackedCards) {
        timeTracker = new TimeTracker();
        timeTracker.start();

        stats = new Stats();
        stats.setCount(0L);
        stats.setMin(1000);
        stats.setMax(0);

        modeMap = new HashMap<>();

        long cumulativeScore = 0L;

        highestHandString = "";

        for (long sim = 0L; sim < nSimulations; sim++) {
            Collections.shuffle(stackedCards);
            Deque<Card> stacked = new LinkedList<>(stackedCards);
            cumulativeScore += simulate(stacked, stats);
        }

        double mean = cumulativeScore / (double) stats.getCount();
        stats.setMean(mean);
        stats.computeMode(modeMap);

        timeTracker.stop();
    }

    private int simulate(Deque<Card> cards, Stats stats) {
        int cumulativeScore = 0;
        Card[][] squares = dealSquares(cards);
        Card starter = cards.poll();
        while (starter != null) {
            int totalScore = 0;
            int[] rowScores = new int[4];
            for (int i = 0; i < 4; i++) {
                Card[] handCards = new Card[4];
                for (int j = 0; j < 4; j++) {
                    handCards[j] = squares[i][j];
                }
                CribbageHand hand = new CribbageHand(handCards, starter);
                int score = hand.scoreHand();
                rowScores[i] = score;
                totalScore += score;
            }

            List<CribbageHand> allHands = new ArrayList<>();

            // columns
            int[] columnScores = new int[4];
            for (int j = 0; j < 4; j++) {
                Card[] handCards = new Card[4];
                for (int i = 0; i < 4; i++) {
                    handCards[i] = squares[i][j];
                }
                CribbageHand hand = new CribbageHand(handCards, starter);
                allHands.add(hand);
                int score = hand.scoreHand();
                columnScores[j] = score;
                totalScore += score;
            }

            if (totalScore > stats.getMax()) {
                if (totalScore > 90) {
                    highestHandString = "   ";
                    highestHandString += starter.getShorthand() + " " + starter.getShorthand() + " " + starter.getShorthand() + " " + starter.getShorthand() + "\n";
                    for (int i = 0; i < 4; i++) {
                        Card[] handCards = new Card[4];
                        for (int j = 0; j < 4; j++) {
                            handCards[j] = squares[i][j];
                            highestHandString += j == 0 ? starter.getShorthand() : "";
                            highestHandString += " " + squares[i][j].getShorthand();
                        }
                        CribbageHand hand = new CribbageHand(handCards, starter);
                        highestHandString += " " + String.format("%2d", hand.scoreHand()) + "\n";
                    }

                    highestHandString += "   ";
                    for (int j = 0; j < 4; j++) {
                        Card[] handCards = new Card[4];
                        for (int i = 0; i < 4; i++) {
                            handCards[i] = squares[i][j];
                        }
                        CribbageHand hand = new CribbageHand(handCards, starter);
                        int score = hand.scoreHand();
                        highestHandString += String.format("%2d", hand.scoreHand()) + " ";
                    }

                    highestHandString += "\nTotal: " + totalScore;
                }
            }

            stats.suggestMin(totalScore);
            stats.suggestMax(totalScore);
            updateModeMap(totalScore);
            cumulativeScore += totalScore;
            stats.incrementCount();

            starter = cards.poll();
            if (totalScore < 90) {
                starter = null;
            }
        }

        return cumulativeScore;
    }

    private Card[][] dealSquares(Deque<Card> cards) {
        Card[][] squares = new Card[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                squares[i][j] = cards.poll();
            }
        }
        return squares;
    }

    private void updateModeMap(int score) {
        Long value = Long.valueOf(0);
        Integer key = Integer.valueOf(score);
        if (modeMap.containsKey(key)) {
            value = modeMap.get(key);
        }
        modeMap.put(key, Long.valueOf(value.longValue() + 1L));
    }

    public String getHighestHandString() {
        return highestHandString;
    }

    public void printStats(String title) {
        System.out.println(title + " elapsed time: " + timeTracker.getElapsedTimeFormatted());
        stats.printStats(title);
    }

    public void printHistogram() {
        // Find the maximum value for scaling (optional) or just print directly
        long maxValue = modeMap.values().stream().max(Long::compare).orElse(1L);

        System.out.println("Score counts:");
        // Print each key-value pair as a histogram bar
        for (Map.Entry<Integer, Long> entry : modeMap.entrySet()) {
            int key = entry.getKey();
            long frequency = entry.getValue();

            // Create the bar using asterisks
//            String bar = "=".repeat((int) value); // Cast to int if values are small
//            System.out.printf("Score %2d (%4d): %s%n", key, value, bar);

            double percent = frequency / (double) stats.getCount() * 100.0;

            // Calculate "1 in N" (avoid division by zero)
            double chances = frequency > 0 ? (double) stats.getCount() / frequency : Double.POSITIVE_INFINITY;
            String oneInN = String.format("1 in %12s %s", NumberFormat.getInstance().format(chances), "hands");
            System.out.printf("  %3d : %9s %7.2f%%  (%s)%n", key, NumberFormat.getInstance().format(frequency), percent, oneInN);
        }
    }

}
