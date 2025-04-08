package dev.newpower.cards.sim;

import dev.newpower.cards.model.Card;
import dev.newpower.cards.model.Deck;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class CribbageSquaresSimulatorTest {

    @Test
    public void testCribbageSquaresSimulator() {
        long nSimulations = 1_000_000_000L;
        long seed = new SecureRandom().nextLong();
        System.out.println("seed:" + seed);
        CribbageSquaresSimulator simulator = new CribbageSquaresSimulator();
        simulator.run(nSimulations, new Random(seed));

        if (simulator.getLowestHand() != null) {
            System.out.println("Lowest Hand:");
            System.out.println(simulator.getLowestHandString());
            System.out.println();
        }

        if (simulator.getHighestHand() != null) {
            System.out.println("Highest Hand:");
            System.out.println(simulator.getHighestHandString());
            System.out.println();
        }

        simulator.printStats();
    }

    @Test
    public void testCribbageSquaresSimulatorWithStackedDeck4through8() {
        long nSimulations = 100000000L;
        long seed = 1L;
        CribbageSquaresSimulator simulator = new CribbageSquaresSimulator();

        Deck deck = new Deck();
        List<Card> stacked = new ArrayList<>(deck.getDeck());
        for (int i = 51; i >= 0; i--) {
            Card card = stacked.get(i);
            int rank = card.getRankNumeric();
            if (rank < 4 || rank > 8) {
                stacked.remove(i);
            }
        }

        simulator.runStacked(nSimulations, new Random(seed), stacked);
        simulator.printStats("Cribbage Squares Simulation (Stacked Deck)");
        System.out.println("Highest Hand:");
        System.out.println(simulator.getHighestHandString());
        simulator.printStats();
    }

    @Test
    public void testCribbageSquaresSimulatorWithStackedDeck5sFace() {
        long nSimulations = 1000000000L;
        long seed = 131L;
        CribbageSquaresSimulator simulator = new CribbageSquaresSimulator();

        Deck deck = new Deck();
        List<Card> stacked = new ArrayList<>(deck.getDeck());
        for (int i = 51; i >= 0; i--) {
            Card card = stacked.get(i);
            int rank = card.getRankNumeric();
            if (rank != 5 && rank < 10) {
                stacked.remove(i);
            }
        }

        simulator.runStacked(nSimulations, new Random(seed), stacked);
        simulator.printStats("Cribbage Squares Simulation (Stacked Deck)");
        System.out.println("Highest Hand:");
        System.out.println(simulator.getHighestHandString());
        simulator.printStats();
    }

}