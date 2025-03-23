package dev.newpower.cards.controller;

import dev.newpower.cards.model.Card;
import dev.newpower.cards.model.Constants;
import dev.newpower.cards.model.Deck;
import dev.newpower.cards.model.ShuffleStats;
import dev.newpower.cards.util.Hasher;
import dev.newpower.cards.util.Images;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class ShuffleView extends JFrame {

    private final Panel panel;

    public ShuffleView(JButton...buttons) {
        setTitle("Shuffle");
        setSize(1290, 624);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setIconImage(new ImageIcon(getClass().getResource("/images/png/poker_chip_100.png")).getImage());
        setLayout(new BorderLayout());
        this.panel = new Panel(new Deck());
        add(panel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        add(buttonPanel, BorderLayout.SOUTH);
        for (JButton button : buttons) {
            buttonPanel.add(button);
        }
        setVisible(true);
    }

    public void shuffle() {
        panel.shuffle();
        validate();
        repaint();
    }

    public void shuffleSimple() {
        panel.shuffleSimple();
        validate();
        repaint();
    }

    public void riffle() {
        panel.riffle();
        validate();
        repaint();
    }

    public void cut() {
        panel.cut();
        validate();
        repaint();
    }

    public void resetDeck() {
        panel.resetDeck();
        validate();
        repaint();
    }

    private class Panel extends JPanel {
        private Images images = new Images();
        private BufferedImage backgroundImage;

        private Deck deck;

        private ShuffleStats shuffleStats = new ShuffleStats();

        Panel(Deck deck) {
            this.deck = deck;
            try {
                backgroundImage = ImageIO.read(getClass().getResource("/images/png/poker_felt.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            drawTableBackground(g);

            drawCards(g);
        }

        void resetDeck() {
            shuffleStats.reset();
            this.deck.reset();
        }

        void shuffle() {
            ShuffleStats stats = new ShuffleStats();
            this.deck.shuffle(stats);
            shuffleStats.setnJavaShuffles(shuffleStats.getnJavaShuffles() + stats.getnJavaShuffles());
            shuffleStats.setnRiffles(shuffleStats.getnRiffles() + stats.getnRiffles());
            shuffleStats.setnCuts(shuffleStats.getnCuts() + stats.getnCuts());
        }

        void shuffleSimple() {
            shuffleStats.incrementJavaShuffles();
            this.deck.shuffleSimple();
        }

        void riffle() {
            shuffleStats.incrementRiffles();
            this.deck.riffleShuffle();
        }

        void cut() {
            shuffleStats.incrementCuts();
            this.deck.cutDeck();
            validate();
            repaint();
        }

        private void drawCards(Graphics g) {
            int midX = this.getWidth() / 2 - Constants.BOARD_CARD_SPACING * 4;
            int midY = this.getHeight() / 2;
            Dimension boardCardDimension = getBoardCardDimension();
            int cardWidth = boardCardDimension.width;
            int cardHeight = boardCardDimension.height;

            int boardWidth = this.getWidth() - (this.getWidth() / 10);
            double x = midX - boardWidth / 2;
            int y = midY - cardHeight / 2;
            double xIncrement = boardWidth / 52.0;

            List<Card> cards = deck.getDeck();
            for (Card card : cards) {
                ImageIcon icon = images.createScaledImageIconWithWhiteBackground(card.getPngImagePath(), cardWidth, cardHeight);
                drawCard(g, icon, (int)Math.round(x), y, cardWidth, cardHeight);
                x += xIncrement;
            }

            Font font = g.getFont().deriveFont(Font.PLAIN, 14);
            g.setFont(font);
            FontMetrics fontMetrics = g.getFontMetrics(g.getFont());
            g.setColor(Color.WHITE);

            // Show shuffle stats
            String stats = shuffleStats.toString();
            Rectangle2D rect = fontMetrics.getStringBounds(stats, g);
            int buffer = 4;
            int buffer2 = buffer * 2;
            int labelX = (int)(midX - rect.getWidth() / 2);
            int labelY = y + cardHeight + Constants.BOARD_CARD_SPACING * 3;
            g.drawString(stats, labelX, labelY + fontMetrics.getAscent());

            // Show hash
            String hash = null;
            try {
                hash = "Hash: " + Hasher.hashDeck(deck);
                rect = fontMetrics.getStringBounds(hash, g);
                labelX = (int)(midX - rect.getWidth() / 2);
                labelY += fontMetrics.getAscent();
                g.drawString(hash, labelX, labelY + fontMetrics.getAscent());
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

        private void drawCard(Graphics g, ImageIcon icon, int x, int y, int cardWidth, int cardHeight) {
            g.drawImage(icon.getImage(), x, y, this);
            g.setColor(Color.lightGray);
            g.drawRect(x, y, cardWidth, cardHeight);
        }

        private void drawTableBackground(Graphics g) {
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        }

        private Dimension getBoardCardDimension() {
            double cardWidth = this.getWidth() * Constants.BOARD_CARD_WIDTH_FRACTION;
            double cardHeight = cardWidth / Constants.CARD_RATIO;
            return new Dimension((int) Math.round(cardWidth), (int) Math.round(cardHeight));
        }

    }

}
