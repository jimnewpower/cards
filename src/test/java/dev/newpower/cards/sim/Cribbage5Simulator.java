package dev.newpower.cards.sim;

import dev.newpower.cards.games.cribbage.CribbageHand;
import dev.newpower.cards.model.Card;
import dev.newpower.cards.model.Deck;

import java.text.NumberFormat;
import java.util.*;

public class Cribbage5Simulator {

    private Stats stats;

    private Map<Integer, Long> modeMap;

    private List<CribbageHand> highest;

    public void run(long nSimulations, Random random) {
        int min = 200;
        int max = 0;
        modeMap = new HashMap<>();
        long count = 0L;

        long scoreTotal = 0L;

        long cumulativeScore = 0L;

        for (int sim = 0; sim < nSimulations; sim++) {
            // deal 5 random 4-card hands that all share a starter card
            List<CribbageHand> hands = new ArrayList<>();
            Deck deck = new Deck(random);
            LinkedList<Card> cards = deck.shuffle();
            Card starter = cards.poll();
            int simScore = 0;
            for (int i = 0; i < 5; i++) {
                Card[] handCards = new Card[4];
                handCards[0] = cards.poll();
                handCards[1] = cards.poll();
                handCards[2] = cards.poll();
                handCards[3] = cards.poll();
                CribbageHand hand = new CribbageHand(handCards, starter);
                int score = hand.scoreHand();
                simScore += score;
                hands.add(hand);
            }

            if (simScore < min) {
                min = simScore;
            }
            if (simScore > max) {
                max = simScore;
                highest = hands;
            }

            updateModeMap(simScore);

            cumulativeScore += simScore;
            ++count;
        }

        stats = new Stats();
        stats.setCount(count);
        stats.setMin(min);
        stats.setMax(max);

        double mean = scoreTotal / (double) count;
        stats.setMean(mean);

        int mode = -1;
        long most = 0L;
        for (Integer score : modeMap.keySet()) {
            if (modeMap.get(score).longValue() > most) {
                most = modeMap.get(score).longValue();
                mode = score.intValue();
            }
        }
        stats.setMode(mode);

    }

    private void updateModeMap(int score) {
        Long value = Long.valueOf(0);
        Integer key = Integer.valueOf(score);
        if (modeMap.containsKey(key)) {
            value = modeMap.get(key);
        }
        modeMap.put(key, Long.valueOf(value.longValue() + 1L));
    }

    public void printStats(String title) {
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
            String oneInN = String.format("1 in %11.2f %s", chances, "hands");
            System.out.printf("  %2d : %10s %7.2f%%  (%s)%n", key, NumberFormat.getInstance().format(frequency), percent, oneInN);
        }
    }

    public void printHighestHand() {
        if (highest == null) {
            System.out.println("Highest is null.");
        }
        System.out.println("Highest hand:");
        for (CribbageHand hand : highest) {
            System.out.println(hand.toString());
        }
    }
}
