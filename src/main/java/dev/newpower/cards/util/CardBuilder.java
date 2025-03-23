package dev.newpower.cards.util;

import dev.newpower.cards.model.Card;
import dev.newpower.cards.model.Rank;
import dev.newpower.cards.model.Suit;

public class CardBuilder {

    public static CardBuilder Ace() {
        CardBuilder builder = new CardBuilder();
        builder.rank = Rank.ACE;
        return builder;
    }

    public static CardBuilder King() {
        CardBuilder builder = new CardBuilder();
        builder.rank = Rank.KING;
        return builder;
    }

    public static CardBuilder Queen() {
        CardBuilder builder = new CardBuilder();
        builder.rank = Rank.QUEEN;
        return builder;
    }

    public static CardBuilder Jack() {
        CardBuilder builder = new CardBuilder();
        builder.rank = Rank.JACK;
        return builder;
    }

    public static CardBuilder Ten() {
        CardBuilder builder = new CardBuilder();
        builder.rank = Rank.TEN;
        return builder;
    }

    public static CardBuilder Nine() {
        CardBuilder builder = new CardBuilder();
        builder.rank = Rank.NINE;
        return builder;
    }

    public static CardBuilder Eight() {
        CardBuilder builder = new CardBuilder();
        builder.rank = Rank.EIGHT;
        return builder;
    }

    public static CardBuilder Seven() {
        CardBuilder builder = new CardBuilder();
        builder.rank = Rank.SEVEN;
        return builder;
    }

    public static CardBuilder Six() {
        CardBuilder builder = new CardBuilder();
        builder.rank = Rank.SIX;
        return builder;
    }

    public static CardBuilder Five() {
        CardBuilder builder = new CardBuilder();
        builder.rank = Rank.FIVE;
        return builder;
    }

    public static CardBuilder Four() {
        CardBuilder builder = new CardBuilder();
        builder.rank = Rank.FOUR;
        return builder;
    }

    public static CardBuilder Three() {
        CardBuilder builder = new CardBuilder();
        builder.rank = Rank.THREE;
        return builder;
    }

    public static CardBuilder Two() {
        CardBuilder builder = new CardBuilder();
        builder.rank = Rank.TWO;
        return builder;
    }

    private Rank rank;
    private Suit suit;

    public CardBuilder() {
    }

    public static Card of(Rank rank, Suit suit) {
        return new Card(rank, suit);
    }

    public static Card of(char rank, char suit) {
        Rank r = Rank.parse(rank);
        Suit s = Suit.parse(suit);
        return of(r, s);
    }

    public static Card of(String rankSuitStr) {
        if (rankSuitStr == null || rankSuitStr.length() != 2) {
            throw new IllegalArgumentException("rankSuitStr must contain two characters: Rank(r) and Suit(s), e.g. \"KS\".");
        }
        char r = rankSuitStr.charAt(0);
        char s = rankSuitStr.charAt(1);
        return of(r, s);
    }

    public Card ofHearts() {
        this.suit = Suit.HEARTS;
        return build();
    }

    public Card ofDiamonds() {
        this.suit = Suit.DIAMONDS;
        return build();
    }

    public Card ofClubs() {
        this.suit = Suit.CLUBS;
        return build();
    }

    public Card ofSpades() {
        this.suit = Suit.SPADES;
        return build();
    }

    public Card build() {
        return new Card(rank, suit);
    }
}
