package dev.newpower.cards.sim;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class CribbageSquaresSimulatorTest {

    @Test
    public void testCribbageSquaresSimulator() {
        long nSimulations = 1000000L;
        CribbageSquaresSimulator simulator = new CribbageSquaresSimulator();
        simulator.run(nSimulations, new Random(1L));
        simulator.printStats("Cribbage Squares Simulation");
        System.out.println("Highest Hand:");
        System.out.println(simulator.getHighestHandString());
    }
}