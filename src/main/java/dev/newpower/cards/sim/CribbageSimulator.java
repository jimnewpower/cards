package dev.newpower.cards.sim;

import dev.newpower.cards.games.cribbage.CribbageHand;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CribbageSimulator {

    int min;
    int max;
    double mean;
    int mode;
    long count;
    Map<Integer, Long> modeMap;

    public CribbageSimulator() {
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public double getMean() {
        return mean;
    }

    public int getMode() {
        return mode;
    }

    public long getCount() {
        return count;
    }

    public Map<Integer, Long> getModeMap() {
        return modeMap;
    }

    public void run(long nSimulations, Random random) {
        min = 30;
        max = 0;
        modeMap = new HashMap<>();
        count = 0L;

        long scoreTotal = 0L;

        for (int i = 0; i < nSimulations; i++) {
            CribbageHand hand = CribbageHand.dealRandom(random);
            int score = hand.scoreHand();

            if (score > max) {
                max = score;
            }
            if (score < min) {
                min = score;
            }

            Long value = Long.valueOf(0);
            Integer key = Integer.valueOf(score);
            if (modeMap.containsKey(key)) {
                value = modeMap.get(key);
            }
            modeMap.put(key, Long.valueOf(value.longValue() + 1L));

            scoreTotal += score;
            ++count;
        }

        mean = scoreTotal / (double) count;

        long most = 0L;
        for (Integer score : modeMap.keySet()) {
            if (modeMap.get(score).longValue() > most) {
                most = modeMap.get(score).longValue();
                mode = score.intValue();
            }
        }
    }

    public void printStats() {
        System.out.println("Cribbage Simulation:");
        System.out.println("count : " + NumberFormat.getInstance().format(count));
        System.out.println("min   : " + getMin());
        System.out.println("max   : " + getMax());
        System.out.println("mean  : " + getMean());
        System.out.println("mode  : " + getMode());
//        modeMap.forEach((key, value) -> System.out.println("Score: " + key + ", Count: " + value));
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

            double percent = frequency / (double) count * 100.0;

            // Calculate "1 in N" (avoid division by zero)
            double chances = frequency > 0 ? (double) count / frequency : Double.POSITIVE_INFINITY;
            String oneInN = String.format("1 in %11.2f %s", chances, "hands");
            System.out.printf("  %2d : %10s %7.2f%%  (%s)%n", key, NumberFormat.getInstance().format(frequency), percent, oneInN);
        }
    }

}
