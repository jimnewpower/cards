package dev.newpower.cards.games.cribbage;

import dev.newpower.cards.model.Card;
import dev.newpower.cards.model.Deck;
import dev.newpower.cards.model.Suit;
import dev.newpower.cards.util.Hasher;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

public class CribbageHand {

    public static CribbageHand deal(Deck deck) {
        LinkedList<Card> shuffled = deck.shuffle();
        List<Card> list = new ArrayList<>(deck.getDeck());
        Card[] hand = new Card[4];
        for (int i = 0; i < 4; i++) {
            hand[i] = shuffled.poll();
            list.remove(hand[i]);
        }

        SecureRandom random = new SecureRandom();
        int randomHalf = list.size() / 2;
        int randomOffset = random.nextInt(0, randomHalf - 2);
        randomOffset = random.nextBoolean() ? -randomOffset : randomOffset;
        Card starter = list.get(randomHalf + randomOffset);

        return new CribbageHand(hand, starter);
    }

    private final Card[] hand;
    private final Card starter;

    public CribbageHand(Card[] hand, Card starter) {
        this.hand = Arrays.copyOf(hand, 4);
        this.starter = starter;
    }

    @Override
    public String toString() {
        return "CribbageHand{" +
                "hand=" + Arrays.toString(hand) +
                ", starter=" + starter +
                "} score=" + scoreHand();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CribbageHand that = (CribbageHand) o;
        return Arrays.equals(hand, that.hand) && Objects.equals(starter, that.starter);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(starter);
        result = 31 * result + Arrays.hashCode(hand);
        return result;
    }

    public int scoreHand() {
        Card[] fullHand = new Card[5];
        System.arraycopy(hand, 0, fullHand, 0, 4);
        fullHand[4] = starter;

        int score = 0;
        score += countFifteens(fullHand);
        score += countPairs(fullHand);
        score += countRuns(fullHand);
        score += countFlush();
        score += countNobs();

        return score;
    }

    // Count combinations that add to 15
    private int countFifteens(Card[] hand) {
        int score = 0;
        int n = hand.length;

        // Use bitmask to check all possible subsets
        for (int mask = 1; mask < (1 << n); mask++) {
            int sum = 0;
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    // Face cards (J, Q, K) count as 10
                    sum += Math.min(hand[i].getRankNumeric(), 10);
                }
            }
            if (sum == 15) score += 2;
        }
        return score;
    }

    // Count pairs (2 points each)
    private int countPairs(Card[] hand) {
        int score = 0;
        for (int i = 0; i < hand.length - 1; i++) {
            for (int j = i + 1; j < hand.length; j++) {
                if (hand[i].getRankNumeric() == hand[j].getRankNumeric()) {
                    score += 2;
                }
            }
        }
        return score;
    }

    private static int countRuns(Card[] hand) {
        int score = 0;
        int[] ranks = new int[hand.length];
        for (int i = 0; i < hand.length; i++) {
            ranks[i] = hand[i].getRankNumeric();
        }
        java.util.Arrays.sort(ranks); // e.g., 11,11,12,12,13

        int i = 0;
        while (i < ranks.length) {
            int runStart = i;
            int[] freq = new int[14]; // Frequency of ranks in this run
            freq[ranks[i]]++;

            // Build the run
            while (i + 1 < ranks.length && (ranks[i + 1] == ranks[i] || ranks[i + 1] == ranks[i] + 1)) {
                i++;
                freq[ranks[i]]++;
            }

            // Count consecutive cards in the run
            int runLength = 0;
            int multiplier = 1;
            for (int r = 1; r < 14; r++) {
                if (freq[r] > 0) {
                    if (runLength > 0 && r != ranks[runStart] + runLength) {
                        break; // Gap in sequence
                    }
                    runLength += 1;
                    multiplier *= freq[r]; // Multiply by number of each rank
                }
            }

            // Score runs of 3 or more
            if (runLength >= 3) {
                score += runLength * multiplier;
            }

            // Move to next unprocessed card
            i++;
        }

        return score;
    }

    // Count flush (4 points if all 4 hand cards match suit, +1 if starter matches)
    private int countFlush() {
        Suit handSuit = hand[0].getSuit();
        for (int i = 1; i < 4; i++) {
            if (hand[i].getSuit() != handSuit) {
                return 0; // No flush if any card differs
            }
        }
        int score = 4; // 4 points for flush in hand
        if (starter.getSuit() == handSuit) {
            score += 1; // Extra point if starter matches
        }
        return score;
    }

    // Count nobs (1 point for Jack in hand matching starter's suit)
    private int countNobs() {
        for (Card card : hand) {
            if (card.getRankNumeric() == 11 && card.getSuit() == starter.getSuit()) { // 11 = Jack
                return 1;
            }
        }
        return 0;
    }

    public String generateUnitTest() {
        String hash = "<hash>";
        try {
            hash = Hasher.hashCribbageHand(this).substring(0, 8);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        int score = scoreHand();
        return """
            @Test
            public void test_%s() {
                Card starter = CardBuilder.of("%s");
                Card[] hand = new Card[] {
                    CardBuilder.of("%s"),
                    CardBuilder.of("%s"),
                    CardBuilder.of("%s"),
                    CardBuilder.of("%s")
                };
                assertEquals(%d, new CribbageHand(hand, starter).scoreHand());
            }
        """.formatted(hash, starter.getShorthand(), hand[0].getShorthand(), hand[1].getShorthand(), hand[2].getShorthand(), hand[3].getShorthand(), score);
    }
}
