package dev.newpower.cards.sim;

import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class Mode {
    private Map<Integer, AtomicLong> modeMap;
    private long count;

    public Mode(int knownMin, int knownMax, int[] exclude) {
        Set<Integer> exclusionSet = new HashSet<>();
        Arrays.stream(exclude).forEach(score -> exclusionSet.add(score));

        modeMap = new HashMap<>();
        for (int score = knownMin; score <= knownMax; score++) {
            if (!exclusionSet.contains(score)) {
                modeMap.put(Integer.valueOf(score), new AtomicLong());
            }
        }
        count = 0L;
    }

    public void add(int score) {
        Integer key = Integer.valueOf(score);
        AtomicLong value = modeMap.get(key);
        value.incrementAndGet();
        ++count;
    }

    public int computeMode() {
        int mode = -1;
        long most = 0L;
        for (Integer score : modeMap.keySet()) {
            if (modeMap.get(score).longValue() > most) {
                most = modeMap.get(score).longValue();
                mode = score.intValue();
            }
        }
        return mode;
    }

    public int getCount(int score) {
        if (!modeMap.containsKey(Integer.valueOf(score))) {
            return -1;
        }
        return modeMap.get(score).intValue();
    }

    public void printHistogram() {
        System.out.println("Score counts:");
        for (Map.Entry<Integer, AtomicLong> entry : modeMap.entrySet()) {
            int key = entry.getKey();
            long frequency = entry.getValue().longValue();
            double percent = frequency / (double) count * 100.0;
            // Calculate "1 in N" (avoid division by zero)
            double chances = frequency > 0 ? (double) count / frequency : Double.POSITIVE_INFINITY;
            String oneInN = String.format("1 in %11.2f %s", chances, "hands");
            System.out.printf("  %2d : %10s %7.2f%%  (%s)%n", key, NumberFormat.getInstance().format(frequency), percent, oneInN);
        }
    }

}
