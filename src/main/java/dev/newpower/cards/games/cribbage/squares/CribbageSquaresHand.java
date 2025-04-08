package dev.newpower.cards.games.cribbage.squares;

import dev.newpower.cards.games.cribbage.CribbageHand;
import dev.newpower.cards.model.Card;

import java.util.*;

public class CribbageSquaresHand {
    private final Card[][] squares = new Card[4][4];
    private Card starter;

    public CribbageSquaresHand(Deque<Card> cards) {
        dealSquares(cards);
        starter = cards.poll();
    }

    public CribbageSquaresHand(CribbageSquaresHand other) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                squares[i][j] = other.squares[i][j];
            }
        }
        this.starter = other.starter;
    }

    private Card[][] dealSquares(Deque<Card> cards) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                squares[i][j] = cards.poll();
            }
        }
        return squares;
    }

    public Card[][] getSquares() {
        return squares;
    }

    public Card getStarter() {
        return starter;
    }

    public void setStarter(Card starter) {
        this.starter = starter;
    }

    public int computeScore() {
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

        // columns
        int[] columnScores = new int[4];
        for (int j = 0; j < 4; j++) {
            Card[] handCards = new Card[4];
            for (int i = 0; i < 4; i++) {
                handCards[i] = squares[i][j];
            }
            CribbageHand hand = new CribbageHand(handCards, starter);
            int score = hand.scoreHand();
            columnScores[j] = score;
            totalScore += score;
        }

        return totalScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CribbageSquaresHand that = (CribbageSquaresHand) o;
        return Arrays.equals(squares, that.squares) && Objects.equals(starter, that.starter);
    }

    @Override
    public int hashCode() {
        Set<Card> grid = new HashSet<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                grid.add(squares[i][j]);
            }
        }
        return Objects.hash(starter, grid);
    }

    @Override
    public String toString() {
        return generateHandString();
    }

    public String generateHandString() {
        String str = "   ";
        str += starter.getShorthand() + " " + starter.getShorthand() + " " + starter.getShorthand() + " " + starter.getShorthand() + "\n";
        for (int i = 0; i < 4; i++) {
            Card[] handCards = new Card[4];
            for (int j = 0; j < 4; j++) {
                handCards[j] = squares[i][j];
                str += j == 0 ? starter.getShorthand() : "";
                str += " " + squares[i][j].getShorthand();
            }
            CribbageHand hand = new CribbageHand(handCards, starter);
            str += " " + String.format("%2d", hand.scoreHand()) + "\n";
        }

        str += "   ";
        for (int j = 0; j < 4; j++) {
            Card[] handCards = new Card[4];
            for (int i = 0; i < 4; i++) {
                handCards[i] = squares[i][j];
            }
            CribbageHand hand = new CribbageHand(handCards, starter);
            int score = hand.scoreHand();
            str += String.format("%2d", hand.scoreHand()) + " ";
        }

        str += "\nTotal: " + computeScore();
        return str;
    }

}
