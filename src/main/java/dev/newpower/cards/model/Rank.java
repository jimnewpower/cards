package dev.newpower.cards.model;

public enum Rank {
    ACE("Ace", 'A', 14),
    KING("King", 'K', 13),
    QUEEN("Queen", 'Q', 12),
    JACK("Jack", 'J', 11),
    TEN("10", 'T', 10),
    NINE("9", '9', 9),
    EIGHT("8", '8', 8),
    SEVEN("7", '7', 7),
    SIX("6", '6', 6),
    FIVE("5", '5', 5),
    FOUR("4", '4', 4),
    THREE("3", '3',3),
    TWO("2", '2', 2);

    private String rank;
    private char c;
    private int rankNumeric;

    Rank(String rank, char c, int rankNumeric) {
        this.rank = rank;
        this.c = c;
        this.rankNumeric = rankNumeric;
    }

    public String getRank() {
        return rank;
    }

    public int getRankNumeric() {
        return rankNumeric;
    }

    public char getChar() {
        return c;
    }

    public static Rank fromInt(int rank) {
        if (rank < 1 || rank > 14) {
            return null;
        }
        if (rank == 1) {
            return Rank.ACE;
        }
        for (Rank r : Rank.values()) {
            if (r.getRankNumeric() == rank) {
                return r;
            }
        }
        return null;
    }

    public static Rank parse(char c) {
        char upper = Character.toUpperCase(c);
        for (Rank rank : values()) {
            if (rank.c == upper) {
                return rank;
            }
        }
        return null;
    }
}
