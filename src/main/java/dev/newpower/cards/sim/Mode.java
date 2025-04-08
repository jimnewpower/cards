package dev.newpower.cards.sim;

import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class Mode {
    private Map<Integer, Long> modeMap;
    private long count;

    public Mode(int knownMin, int knownMax) {
        modeMap = new HashMap<>();
        for (int score = knownMin; score <= knownMax; score++) {
            modeMap.put(Integer.valueOf(score), 0L);
        }
        count = 0L;
    }

    public Mode(int knownMin, int knownMax, int[] exclude) {
        Set<Integer> exclusionSet = new HashSet<>();
        Arrays.stream(exclude).forEach(score -> exclusionSet.add(score));

        modeMap = new HashMap<>();
        for (int score = knownMin; score <= knownMax; score++) {
            if (!exclusionSet.contains(score)) {
                modeMap.put(Integer.valueOf(score), 0L);
            }
        }
        count = 0L;
    }

    public void add(int score) {
        Integer key = Integer.valueOf(score);
        Long value = modeMap.get(key);
        modeMap.put(key, value + 1);
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
        for (Map.Entry<Integer, Long> entry : modeMap.entrySet()) {
            int key = entry.getKey();
            long frequency = entry.getValue().longValue();
            double percent = frequency / (double) count * 100.0;
            // Calculate "1 in N" (avoid division by zero)
            double chances = frequency > 0 ? (double) count / frequency : Double.POSITIVE_INFINITY;
            String oneInN = String.format("1 in %11.2f %s", chances, "hands");
            System.out.printf("  %3d : %10s %7.2f%%  (%s)%n", key, NumberFormat.getInstance().format(frequency), percent, oneInN);
        }
    }

    // Add a score to the histogram
    public void addScore(int score) {
        modeMap.put(score, modeMap.getOrDefault(score, 0L) + 1);
    }

    // Total number of hands
    public long getTotalHands() {
        long total = 0;
        for (Long freq : modeMap.values()) {
            total += freq.longValue();
        }
        return total;
    }

    // Minimum score
    public int getMin() {
        if (modeMap.isEmpty())
            return -1; // Or throw exception
        int max = Collections.max(modeMap.keySet());
        int score = Collections.min(modeMap.keySet());
        while (score <= max) {
            long count = modeMap.get(score);
            if (count > 0) {
                return score;
            }
            ++score;
        }
        return Collections.min(modeMap.keySet());
    }

    // Maximum score
    public int getMax() {
        if (modeMap.isEmpty())
            return -1; // Or throw exception
        int min = Collections.min(modeMap.keySet());
        int score = Collections.max(modeMap.keySet());
        while (score >= min) {
            long count = modeMap.get(score);
            if (count > 0) {
                return score;
            }
            --score;
        }
        return Collections.max(modeMap.keySet());
    }

    // Mean score
    public double getMean() {
        if (modeMap.isEmpty())
            return 0.0;
        double sum = 0;
        long totalHands = getTotalHands();
        for (Map.Entry<Integer, Long> entry : modeMap.entrySet()) {
            sum += entry.getKey() * entry.getValue();
        }
        return sum / totalHands;
    }

    // Mode(s)
    public List<Integer> getModes() {
        if (modeMap.isEmpty())
            return Collections.emptyList();

        List<Integer> modes = new ArrayList<>();
        long maxFrequency = Collections.max(modeMap.values());

        for (Map.Entry<Integer, Long> entry : modeMap.entrySet()) {
            if (entry.getValue() == maxFrequency) {
                modes.add(entry.getKey());
            }
        }
        return modes;
    }

    public double getVariance() {
        if (modeMap.isEmpty())
            return 0.0;

        double mean = getMean();
        double sumSquaredDiff = 0;
        long totalHands = getTotalHands();

        for (Map.Entry<Integer, Long> entry : modeMap.entrySet()) {
            double diff = entry.getKey() - mean;
            sumSquaredDiff += entry.getValue() * diff * diff;
        }
        double variance = sumSquaredDiff / totalHands; // Population variance
        return variance;
    }

    // Standard deviation
    public double getStandardDeviation() {
        return Math.sqrt(getVariance());
    }

    // Percentile (p is between 0 and 100)
    public double getPercentile(double p) {
        if (modeMap.isEmpty() || p < 0 || p > 100)
            return Double.NaN;
        long totalHands = getTotalHands();
        long targetCount = (long) Math.ceil(totalHands * (p / 100.0)); // Number of hands below or at percentile

        // Sort scores
        List<Integer> sortedScores = new ArrayList<>(modeMap.keySet());
        Collections.sort(sortedScores);

        long cumulativeCount = 0;
        for (int score : sortedScores) {
            cumulativeCount += modeMap.get(score);
            if (cumulativeCount >= targetCount) {
                return score; // Return the score where cumulative count meets or exceeds target
            }
        }
        return sortedScores.get(sortedScores.size() - 1); // Fallback to max if logic fails
    }

}
