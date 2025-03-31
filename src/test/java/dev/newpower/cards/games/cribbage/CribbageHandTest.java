package dev.newpower.cards.games.cribbage;

import dev.newpower.cards.model.Card;
import dev.newpower.cards.model.Deck;
import dev.newpower.cards.util.CardBuilder;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CribbageHandTest {

    // Uncomment this to generate some random hands and their test code
//    @Test
//    public void deal() throws UnsupportedEncodingException, NoSuchAlgorithmException {
//        for (int i = 0; i < 1000; i++) {
//            CribbageHand hand = CribbageHand.deal(new Deck());
//            int score = hand.scoreHand();
//            System.out.println(hand.toString() + " " + Hasher.hashCribbageHand(hand));
//            System.out.println(hand.generateUnitTest());
//        }
//    }

    @Test
    public void testNullHand() {
        assertThrows(NullPointerException.class, () -> new CribbageHand(null, CardBuilder.of("AH")));
    }

    @Test
    public void testNullStarter() {
        Card[] hand = new Card[] {
                CardBuilder.of("5D"),
                CardBuilder.of("TH"),
                CardBuilder.of("TD"),
                CardBuilder.of("5C")
        };
        assertThrows(NullPointerException.class, () -> new CribbageHand(hand, null));
    }

    @Test
    public void testWrongSizeHandTooFew() {
        Card[] hand = new Card[] {
                CardBuilder.of("5D"),
                CardBuilder.of("TH"),
                CardBuilder.of("5C")
        };
        Card starter = CardBuilder.of("AC");
        assertThrows(IllegalArgumentException.class, () -> new CribbageHand(hand, starter));
    }

    @Test
    public void testWrongSizeHandTooMany() {
        Card[] hand = new Card[] {
                CardBuilder.of("AD"),
                CardBuilder.of("2H"),
                CardBuilder.of("3C"),
                CardBuilder.of("4C"),
                CardBuilder.of("5C"),
        };
        Card starter = CardBuilder.of("6D");
        assertThrows(IllegalArgumentException.class, () -> new CribbageHand(hand, starter));
    }

    @Test
    public void testDealFromTop() {
        Deck deck = new Deck();
        Random random = new Random(1L);
        CribbageHand hand = CribbageHand.dealFromTop(deck, random);
        assertEquals("CribbageHand{hand=[KING of SPADES, QUEEN of SPADES, JACK of SPADES, TEN of SPADES], starter=NINE of CLUBS} score=9", hand.toString());

        deck = new Deck();
        random = new Random(97L);
        hand = CribbageHand.dealFromTop(deck, random);
        assertEquals("CribbageHand{hand=[KING of SPADES, QUEEN of SPADES, JACK of SPADES, TEN of SPADES], starter=TEN of HEARTS} score=14", hand.toString());
    }

    @Test
    public void testDealRandom() {
        Random random = new Random(1L);
        CribbageHand hand = CribbageHand.dealRandom(random);
        assertEquals("CribbageHand{hand=[ACE of CLUBS, FIVE of DIAMONDS, NINE of SPADES, JACK of SPADES], starter=KING of HEARTS} score=6", hand.toString());

        random = new Random(97L);
        hand = CribbageHand.dealRandom(random);
        assertEquals("CribbageHand{hand=[QUEEN of HEARTS, THREE of DIAMONDS, TEN of CLUBS, EIGHT of CLUBS], starter=THREE of CLUBS} score=2", hand.toString());
    }

    @Test
    public void test29() {
        Card starter = CardBuilder.Five().ofClubs();
        Card[] hand = new Card[] {
            CardBuilder.Five().ofSpades(),
            CardBuilder.Five().ofHearts(),
            CardBuilder.Five().ofDiamonds(),
            CardBuilder.Jack().ofClubs()
        };

        CribbageHand cribbageHand = new CribbageHand(hand, starter);
        assertEquals(29, cribbageHand.scoreHand());
    }

    @Test
    public void test28() {
        Card starter = CardBuilder.Five().ofClubs();
        Card[] hand = new Card[] {
            CardBuilder.Five().ofSpades(),
            CardBuilder.Five().ofHearts(),
            CardBuilder.Five().ofDiamonds(),
            CardBuilder.Jack().ofDiamonds()
        };

        CribbageHand cribbageHand = new CribbageHand(hand, starter);
        assertEquals(28, cribbageHand.scoreHand());
    }

    @Test
    public void test24() {
        Card starter = CardBuilder.Seven().ofDiamonds();
        Card[] hand = new Card[] {
            CardBuilder.Seven().ofSpades(),
            CardBuilder.Eight().ofHearts(),
            CardBuilder.Eight().ofClubs(),
            CardBuilder.Nine().ofSpades()
        };

        CribbageHand cribbageHand = new CribbageHand(hand, starter);
        assertEquals(24, cribbageHand.scoreHand());

        starter = CardBuilder.Four().ofDiamonds();
        hand = new Card[] {
            CardBuilder.Four().ofSpades(),
            CardBuilder.Five().ofHearts(),
            CardBuilder.Five().ofClubs(),
            CardBuilder.Six().ofSpades()
        };

        cribbageHand = new CribbageHand(hand, starter);
        assertEquals(24, cribbageHand.scoreHand());

        starter = CardBuilder.Three().ofSpades();
        hand = new Card[] {
                CardBuilder.Six().ofSpades(),
                CardBuilder.Six().ofHearts(),
                CardBuilder.Six().ofClubs(),
                CardBuilder.Six().ofDiamonds()
        };

        cribbageHand = new CribbageHand(hand, starter);
        assertEquals(24, cribbageHand.scoreHand());

        starter = CardBuilder.Ace().ofSpades();
        hand = new Card[] {
                CardBuilder.Seven().ofSpades(),
                CardBuilder.Seven().ofHearts(),
                CardBuilder.Seven().ofClubs(),
                CardBuilder.Seven().ofDiamonds()
        };

        cribbageHand = new CribbageHand(hand, starter);
        assertEquals(24, cribbageHand.scoreHand());
    }

    @Test
    public void test23() {
        Card starter = CardBuilder.of("5C");
        Card[] hand = new Card[] {
                CardBuilder.of("JH"),
                CardBuilder.of("JC"),
                CardBuilder.of("5H"),
                CardBuilder.of("5D")
        };
        assertEquals(23, new CribbageHand(hand, starter).scoreHand());
    }

    @Test
    public void test22() {
        Card starter = CardBuilder.of("5S");
        Card[] hand = new Card[] {
                CardBuilder.of("5D"),
                CardBuilder.of("TH"),
                CardBuilder.of("TD"),
                CardBuilder.of("5C")
        };
        assertEquals(22, new CribbageHand(hand, starter).scoreHand());
    }

    @Test
    public void test21() {
        Card starter = CardBuilder.of("QH");
        Card[] hand = new Card[] {
                CardBuilder.of("5S"),
                CardBuilder.of("5C"),
                CardBuilder.of("5D"),
                CardBuilder.of("JH")
        };
        assertEquals(21, new CribbageHand(hand, starter).scoreHand());
    }

    @Test
    public void test20() {
        Card starter = CardBuilder.Six().ofSpades();
        Card[] hand = new Card[] {
            CardBuilder.Six().ofClubs(),
            CardBuilder.Nine().ofSpades(),
            CardBuilder.Nine().ofHearts(),
            CardBuilder.Nine().ofDiamonds()
        };

        CribbageHand cribbageHand = new CribbageHand(hand, starter);
        assertEquals(20, cribbageHand.scoreHand());

        starter = CardBuilder.Five().ofSpades();
        hand = new Card[] {
            CardBuilder.Five().ofClubs(),
            CardBuilder.Ten().ofSpades(),
            CardBuilder.Ten().ofHearts(),
            CardBuilder.Ten().ofDiamonds()
        };

        cribbageHand = new CribbageHand(hand, starter);
        assertEquals(20, cribbageHand.scoreHand());

        starter = CardBuilder.Seven().ofSpades();
        hand = new Card[] {
            CardBuilder.Seven().ofClubs(),
            CardBuilder.Eight().ofSpades(),
            CardBuilder.Eight().ofHearts(),
            CardBuilder.Eight().ofDiamonds()
        };

        cribbageHand = new CribbageHand(hand, starter);
        assertEquals(20, cribbageHand.scoreHand());
    }

    @Test
    public void test18() {
        Card starter = CardBuilder.of("4S");
        Card[] hand = new Card[] {
                CardBuilder.of("2C"),
                CardBuilder.of("2D"),
                CardBuilder.of("4D"),
                CardBuilder.of("3D")
        };
        assertEquals(18, new CribbageHand(hand, starter).scoreHand());
    }

    @Test
    public void test17() {
        Card starter = CardBuilder.of("QS");
        Card[] hand = new Card[] {
                CardBuilder.of("TC"),
                CardBuilder.of("5C"),
                CardBuilder.of("5H"),
                CardBuilder.of("JD")
        };
        assertEquals(17, new CribbageHand(hand, starter).scoreHand());
    }

    @Test
    public void test16() {
        Card starter = CardBuilder.Jack().ofDiamonds();
        Card[] hand = new Card[] {
            CardBuilder.Jack().ofHearts(),
            CardBuilder.Queen().ofHearts(),
            CardBuilder.Queen().ofDiamonds(),
            CardBuilder.King().ofSpades()
        };

        CribbageHand cribbageHand = new CribbageHand(hand, starter);
        assertEquals(16, cribbageHand.scoreHand());
    }

    @Test
    public void test15() {
        Card starter = CardBuilder.of("6H");
        Card[] hand = new Card[] {
                CardBuilder.of("7S"),
                CardBuilder.of("5D"),
                CardBuilder.of("7H"),
                CardBuilder.of("7D")
        };
        assertEquals(15, new CribbageHand(hand, starter).scoreHand());
    }

    @Test
    public void test14() {
        Card starter = CardBuilder.of("7H");
        Card[] hand = new Card[] {
                CardBuilder.of("8C"),
                CardBuilder.of("7C"),
                CardBuilder.of("AS"),
                CardBuilder.of("9C")
        };
        assertEquals(14, new CribbageHand(hand, starter).scoreHand());
    }

    @Test
    public void test13() {
        Card starter = CardBuilder.of("4H");
        Card[] hand = new Card[] {
                CardBuilder.of("5D"),
                CardBuilder.of("6D"),
                CardBuilder.of("9D"),
                CardBuilder.of("TD")
        };
        assertEquals(13, new CribbageHand(hand, starter).scoreHand());
    }

    @Test
    public void test12() {
        Card starter = CardBuilder.of("2C");
        Card[] hand = new Card[] {
                CardBuilder.of("4C"),
                CardBuilder.of("3S"),
                CardBuilder.of("8C"),
                CardBuilder.of("2H")
        };
        assertEquals(12, new CribbageHand(hand, starter).scoreHand());
    }

    @Test
    public void test11() {
        Card starter = CardBuilder.of("7S");
        Card[] hand = new Card[] {
                CardBuilder.of("7C"),
                CardBuilder.of("6H"),
                CardBuilder.of("JS"),
                CardBuilder.of("5D")
        };
        assertEquals(11, new CribbageHand(hand, starter).scoreHand());
    }

    @Test
    public void test10() {
        Card starter = CardBuilder.of("9H");
        Card[] hand = new Card[] {
                CardBuilder.of("7C"),
                CardBuilder.of("9C"),
                CardBuilder.of("4D"),
                CardBuilder.of("8D")
        };
        assertEquals(10, new CribbageHand(hand, starter).scoreHand());
    }

    @Test
    public void test9() {
        Card starter = CardBuilder.of("4S");
        Card[] hand = new Card[] {
                CardBuilder.of("JS"),
                CardBuilder.of("5C"),
                CardBuilder.of("JD"),
                CardBuilder.of("QH")
        };
        assertEquals(9, new CribbageHand(hand, starter).scoreHand());
    }

    @Test
    public void test8() {
        Card starter = CardBuilder.Two().ofDiamonds();
        Card[] hand = new Card[] {
            CardBuilder.Ace().ofSpades(),
            CardBuilder.Three().ofDiamonds(),
            CardBuilder.Queen().ofDiamonds(),
            CardBuilder.Four().ofHearts()
        };

        CribbageHand cribbageHand = new CribbageHand(hand, starter);
        assertEquals(8, cribbageHand.scoreHand());
    }

    @Test
    public void test7() {
        Card starter = CardBuilder.of("AH");
        Card[] hand = new Card[] {
            CardBuilder.of("9C"),
            CardBuilder.of("JD"),
            CardBuilder.of("3C"),
            CardBuilder.of("2H")
        };
        assertEquals(7, new CribbageHand(hand, starter).scoreHand());
    }

    @Test
    public void test6() {
        Card starter = CardBuilder.of("5C");
        Card[] hand = new Card[] {
                CardBuilder.of("5S"),
                CardBuilder.of("3D"),
                CardBuilder.of("AH"),
                CardBuilder.of("9D")
        };
        assertEquals(6, new CribbageHand(hand, starter).scoreHand());
    }

    @Test
    public void test5() {
        Card starter = CardBuilder.of("4C");
        Card[] hand = new Card[] {
                CardBuilder.of("8H"),
                CardBuilder.of("6H"),
                CardBuilder.of("7C"),
                CardBuilder.of("KH")
        };
        assertEquals(5, new CribbageHand(hand, starter).scoreHand());
    }

    @Test
    public void test4() {
        Card starter = CardBuilder.of("2S");
        Card[] hand = new Card[] {
                CardBuilder.of("5H"),
                CardBuilder.of("7S"),
                CardBuilder.of("5D"),
                CardBuilder.of("2D")
        };
        assertEquals(4, new CribbageHand(hand, starter).scoreHand());
    }

    @Test
    public void test3() {
        Card starter = CardBuilder.of("JC");
        Card[] hand = new Card[] {
                CardBuilder.of("3H"),
                CardBuilder.of("4D"),
                CardBuilder.of("TD"),
                CardBuilder.of("QS")
        };
        assertEquals(3, new CribbageHand(hand, starter).scoreHand());
    }

    @Test
    public void test2() {
        Card starter = CardBuilder.of("KD");
        Card[] hand = new Card[] {
                CardBuilder.of("TC"),
                CardBuilder.of("2D"),
                CardBuilder.of("KS"),
                CardBuilder.of("QH")
        };
        assertEquals(2, new CribbageHand(hand, starter).scoreHand());
    }

    @Test
    public void test1() {
        Card starter = CardBuilder.of("6H");
        Card[] hand = new Card[] {
                CardBuilder.of("3C"),
                CardBuilder.of("KS"),
                CardBuilder.of("4D"),
                CardBuilder.of("JH")
        };
        assertEquals(1, new CribbageHand(hand, starter).scoreHand());
    }

    @Test
    public void test0() {
        Card starter = CardBuilder.of("8H");
        Card[] hand = new Card[] {
                CardBuilder.of("JC"),
                CardBuilder.of("2D"),
                CardBuilder.of("4D"),
                CardBuilder.of("6S")
        };
        assertEquals(0, new CribbageHand(hand, starter).scoreHand());
    }

    @Test
    public void test_315bb04f() {
        Card starter = CardBuilder.of("JD");
        Card[] hand = new Card[] {
                CardBuilder.of("8S"),
                CardBuilder.of("3H"),
                CardBuilder.of("TS"),
                CardBuilder.of("3S")
        };
        assertEquals(2, new CribbageHand(hand, starter).scoreHand());
    }

    @Test
    public void test_4d9fee63() {
        Card starter = CardBuilder.of("7H");
        Card[] hand = new Card[] {
                CardBuilder.of("QS"),
                CardBuilder.of("TD"),
                CardBuilder.of("6S"),
                CardBuilder.of("8D")
        };
        assertEquals(5, new CribbageHand(hand, starter).scoreHand());
    }

    @Test
    public void test_1ed56f3a() {
        Card starter = CardBuilder.of("6S");
        Card[] hand = new Card[] {
                CardBuilder.of("KS"),
                CardBuilder.of("4H"),
                CardBuilder.of("JS"),
                CardBuilder.of("9H")
        };
        assertEquals(3, new CribbageHand(hand, starter).scoreHand());
    }

    @Test
    public void test_651f6fff() {
        Card starter = CardBuilder.of("QD");
        Card[] hand = new Card[] {
                CardBuilder.of("6S"),
                CardBuilder.of("7C"),
                CardBuilder.of("AC"),
                CardBuilder.of("JD")
        };
        assertEquals(1, new CribbageHand(hand, starter).scoreHand());
    }



    @Test
    public void test_0a0843b5() {
        Card starter = CardBuilder.of("4D");
        Card[] hand = new Card[] {
                CardBuilder.of("3C"),
                CardBuilder.of("4S"),
                CardBuilder.of("5S"),
                CardBuilder.of("6C")
        };
        assertEquals(14, new CribbageHand(hand, starter).scoreHand());
    }

    @Test
    public void test_08f4c804() {
        Card starter = CardBuilder.of("KH");
        Card[] hand = new Card[] {
                CardBuilder.of("5C"),
                CardBuilder.of("TS"),
                CardBuilder.of("5D"),
                CardBuilder.of("5S")
        };
        assertEquals(20, new CribbageHand(hand, starter).scoreHand());
    }






}