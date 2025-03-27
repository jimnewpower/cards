package dev.newpower.cards.sim;

import org.junit.jupiter.api.Test;

import java.util.Random;

class CribbageSimulatorTest {

    @Test
    public void testSimulator() {
        long nSimulations = 10000L;
        long seed = 1L;
        CribbageSimulator simulator = new CribbageSimulator();
        simulator.run(nSimulations, new Random(seed));
        simulator.printStats();
        simulator.printHistogram();
    }
}