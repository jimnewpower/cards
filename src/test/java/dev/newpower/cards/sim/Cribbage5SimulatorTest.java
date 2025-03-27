package dev.newpower.cards.sim;

import org.junit.jupiter.api.Test;

import java.util.Random;

class Cribbage5SimulatorTest {

    @Test
    public void testCribbage5Simulator() {
        long nSimulations = 10000000L;
        long seed = 1L;
        Cribbage5Simulator simulator = new Cribbage5Simulator();
        simulator.run(nSimulations, new Random(seed));
        simulator.printStats("Cribbage 5 Simulation:");
        simulator.printHistogram();
        simulator.printHighestHand();
    }
}