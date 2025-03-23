package dev.newpower.cards.model;


import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

public class Deck {
    private static final int MIN_SHUFFLE_ITERATIONS = 3;
    private Random random;

    private List<Card> deck;

    private int freshDeckHash;

    public Deck() {
        this.random = new SecureRandom();
        reset();
    }

    public Deck(Random random) {
        this.random = random;
        reset();
    }

    public void reset() {
        deck = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                deck.add(new Card(rank, suit));
            }
        }

        this.freshDeckHash = hashCode();
    }

    @Override
    public String toString() {
        String[] cardStrings = deck.stream().map(Card::toString).toArray(String[]::new);
        return String.join(" ", cardStrings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deck.toArray());
    }

    public void shuffleSimple() {
        Collections.shuffle(deck, random);
    }

    public LinkedList<Card> shuffle(ShuffleStats stats) {
        final int min = MIN_SHUFFLE_ITERATIONS;
        // want a minimum number of each type of shuffle
        while (stats.getnJavaShuffles() < min || stats.getnCuts() < min || stats.getnRiffles() < min) {
            int rand = Math.abs(random.nextInt() % 3);
            switch (rand) {
                case 0 -> {
                    Collections.shuffle(deck, random);
                    stats.incrementJavaShuffles();
                }
                case 1 -> {
                    cutDeck();
                    stats.incrementCuts();
                }
                case 2 -> {
                    riffleShuffle();
                    stats.incrementRiffles();
                }
            }
        }

        return new LinkedList<>(deck);
    }

    public LinkedList<Card> shuffle() {
        return shuffle(new ShuffleStats());
    }

    private int randomCutIndex() {
        int randomHalf = deck.size() / 2;
        int randomOffset = random.nextInt(0, 11);
        randomOffset = random.nextBoolean() ? -randomOffset : randomOffset;
        return randomHalf + randomOffset;
    }

    public void cutDeck() {
        int halfSize = randomCutIndex();

        // Split the deck into two halves
        List<Card> firstHalf = new ArrayList<>(deck.subList(0, halfSize));
        List<Card> secondHalf = new ArrayList<>(deck.subList(halfSize, deck.size()));

        // Reorder the deck by placing the second half before the first half
        deck.clear();
        deck.addAll(secondHalf);
        deck.addAll(firstHalf);
    }

    public void riffleShuffle() {
        int halfSize = randomCutIndex();

        // Split the deck into two halves
        List<Card> firstHalf = new ArrayList<>(deck.subList(0, halfSize));
        List<Card> secondHalf = new ArrayList<>(deck.subList(halfSize, deck.size()));

        deck.clear();

        // Interleave the cards from the two halves
        while (!firstHalf.isEmpty() || !secondHalf.isEmpty()) {
            if (!firstHalf.isEmpty() && (secondHalf.isEmpty() || random.nextBoolean())) {
                deck.add(firstHalf.remove(0));
            }
            if (!secondHalf.isEmpty() && (firstHalf.isEmpty() || random.nextBoolean())) {
                deck.add(secondHalf.remove(0));
            }
        }
    }

    public List<Card> getDeck() {
        return List.of(deck.toArray(new Card[0]));
    }

}