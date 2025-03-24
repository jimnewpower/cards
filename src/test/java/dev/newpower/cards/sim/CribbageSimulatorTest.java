package dev.newpower.cards.sim;

import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class CribbageSimulatorTest {

    @Test
    public void testSimulator() {
        CribbageSimulator simulator = new CribbageSimulator();
        simulator.run(100000L, new Random(1L));
        simulator.printStats();
        simulator.printHistogram();
    }
}