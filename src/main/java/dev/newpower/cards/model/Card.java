package dev.newpower.cards.model;

import java.util.Objects;

public class Card {
    private Rank rank;
    private Suit suit;
    private String svgImagePath;
    private String pngImagePath;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public int getRankNumeric() {
        return rank.getRankNumeric();
    }

    public Suit getSuit() {
        return suit;
    }

    public String getSvgImagePath() {
        if (svgImagePath == null) {
            this.svgImagePath = "/images/svg/" + rank.getRank().toLowerCase() + "_of_" + suit.getName().toLowerCase() + ".svg";
        }
        return svgImagePath;
    }

    public String getPngImagePath() {
        if (pngImagePath == null) {
            this.pngImagePath = "/images/png/" + rank.getRank().toLowerCase() + "_of_" + suit.getName().toLowerCase() + ".png";
        }
        return pngImagePath;
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(rank, card.rank) && Objects.equals(suit, card.suit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, suit);
    }

    public String getShorthand() {
        return "" + rank.getChar() + suit.getChar();
    }
}

