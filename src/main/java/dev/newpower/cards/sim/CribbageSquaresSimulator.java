package dev.newpower.cards.sim;

import dev.newpower.cards.games.cribbage.CribbageHand;
import dev.newpower.cards.model.Card;
import dev.newpower.cards.model.Deck;

import java.util.*;

public class CribbageSquaresSimulator {
    private Stats stats;

    private String highestHandString;

    public void run(long nSimulations, Random random) {
        int min = 240;
        int max = 0;
        long count = 0L;

        long cumulativeScore = 0L;

        highestHandString = "";

        for (long sim = 0L; sim < nSimulations; sim++) {
            Deck deck = new Deck(random);
            LinkedList<Card> cards = deck.shuffle();

            Card[][] squares = new Card[4][4];
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    squares[i][j] = cards.poll();
                }
            }
            Card starter = cards.poll();

            int totalScore = 0;
            int[] rowScores = new int[4];
            for (int i = 0; i < 4; i++) {
                Card[] handCards = new Card[4];
                for (int j = 0; j < 4; j++) {
                    handCards[j] = squares[i][j];
                }
                CribbageHand hand = new CribbageHand(handCards, starter);
                int score = hand.scoreHand();
                rowScores[i] = score;
                totalScore += score;
            }

            List<CribbageHand> allHands = new ArrayList<>();

            // columns
            int[] columnScores = new int[4];
            for (int j = 0; j < 4; j++) {
                Card[] handCards = new Card[4];
                for (int i = 0; i < 4; i++) {
                    handCards[i] = squares[i][j];
                }
                CribbageHand hand = new CribbageHand(handCards, starter);
                allHands.add(hand);
                int score = hand.scoreHand();
                columnScores[j] = score;
                totalScore += score;
            }

            if (totalScore < min) {
                min = totalScore;
            }
            if (totalScore > max) {
                max = totalScore;
                highestHandString = "   ";
                highestHandString += starter.getShorthand() + " " + starter.getShorthand() + " " + starter.getShorthand() + " " + starter.getShorthand() + "\n";
                for (int i = 0; i < 4; i++) {
                    Card[] handCards = new Card[4];
                    for (int j = 0; j < 4; j++) {
                        handCards[j] = squares[i][j];
                        highestHandString += j == 0 ? starter.getShorthand() : "";
                        highestHandString += " " + squares[i][j].getShorthand();
                    }
                    CribbageHand hand = new CribbageHand(handCards, starter);
                    highestHandString += " " + String.format("%2d", hand.scoreHand()) + "\n";
                }

                highestHandString += "   ";
                for (int j = 0; j < 4; j++) {
                    Card[] handCards = new Card[4];
                    for (int i = 0; i < 4; i++) {
                        handCards[i] = squares[i][j];
                    }
                    CribbageHand hand = new CribbageHand(handCards, starter);
                    int score = hand.scoreHand();
                    highestHandString += String.format("%2d", hand.scoreHand()) + " ";
                }

                highestHandString += "\nTotal: " + totalScore;
            }

            cumulativeScore += totalScore;
            ++count;
        }

        double mean = cumulativeScore / (double) count;

        stats = new Stats();
        stats.setCount(count);
        stats.setMin(min);
        stats.setMax(max);
        stats.setMean(mean);
    }

    public String getHighestHandString() {
        return highestHandString;
    }

    public void printStats(String title) {
        stats.printStats(title);
    }
}
