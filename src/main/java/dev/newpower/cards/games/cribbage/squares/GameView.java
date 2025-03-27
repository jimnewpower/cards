package dev.newpower.cards.games.cribbage.squares;

import dev.newpower.cards.games.cribbage.CribbageHand;
import dev.newpower.cards.model.Card;
import dev.newpower.cards.model.Constants;
import dev.newpower.cards.model.Deck;
import dev.newpower.cards.util.Images;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends JFrame {
    private final Panel panel;

    public GameView(Random random) {
        setLayout(new BorderLayout());
        setTitle("Cribbage Squares");
        setSize(600, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setIconImage(new ImageIcon(getClass().getResource("/images/png/poker_chip_100.png")).getImage());

        this.panel = new Panel(random);
        add(panel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        AbstractButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(ae -> panel.newGame());

        AbstractButton helpButton = new JButton("Help...");
        helpButton.addActionListener(ae -> showHelp());

        buttonPanel.add(newGameButton);
        buttonPanel.add(helpButton);

        setVisible(true);
    }

    private void showHelp() {
        String message = """
                1. Tap deck to deal first card, or select New Game to deal first card.
                2. Scores will be added up in all rows and columns, creating 8 hands.
                3. Hover mouse over the grid location and click to drop the card.
                4. The next card will be dealt automatically after dropping the previous card.
                5. After last card is placed, the starter card will be cut automatically and scores tallied.
                """;
        JOptionPane.showMessageDialog(this, message, "Cribbage Squares Help", JOptionPane.QUESTION_MESSAGE);
    }

    private class Panel extends JPanel implements MouseListener {
        private Images images = new Images();
        private BufferedImage backgroundImage;

        private Deck deck;
        private List<Card> cards;

        private Card[][] squares = new Card[4][4];
        private Rectangle[][] rectangles = new Rectangle[4][4];
        private int xHighlight = -1;
        private int yHighlight = -1;

        private Rectangle deckRectangle;

        private Card currentCard;
        private Card starterCard;

        private int[] rowScores;
        private int[] columnScores;

        private boolean rabbitHunting = false;

        Panel(Random random) {
            deck = new Deck(random);

            try {
                backgroundImage = ImageIO.read(getClass().getResource("/images/png/poker_felt.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            addMouseListener(this);
            // Add mouse motion listener to detect when mouse enters/exits the rectangle
            addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    Point mousePos = e.getPoint();

                    xHighlight = -1;
                    yHighlight = -1;
                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 4; j++) {
                            if (rectangles[i][j] != null && rectangles[i][j].contains(mousePos.x, mousePos.y)) {
                                xHighlight = i;
                                yHighlight = j;
                                break;
                            }
                        }
                    }

                    panel.repaint(); // Repaint to update visual feedback
                }
            });

            setFocusable(true); // needed for key listener
            requestFocusInWindow();
            addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                    // This fires when a key is typed (pressed and released)
                    if (e.getKeyChar() == 'n') {
                        // Action when 'n' is typed
                        rabbitHunting = true;
                        currentCard = null;
                        starterCard = null;
                        deal();
                        rabbitHunting = false;
                    }
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    // This fires when a key is pressed (optional)
                    // You could use e.getKeyCode() == KeyEvent.VK_N here instead
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    // This fires when a key is released (optional)
                }
            });

            newGame();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;

            // Enable anti-aliasing for smoother text and shapes
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            drawTableBackground(g);

            drawDeck(g);
            drawCurrentCard(g);
            drawGrid(g);
            drawScores(g);
        }

        private void drawScores(Graphics g) {
            if (!isGridFull()) {
                return;
            }

            Dimension cardDimension = getBoardCardDimension();
            int cardWidth = cardDimension.width;
            int cardHeight = cardDimension.height;

            int x = this.getWidth() / 6;
            int y = this.getHeight() / 5;

            FontMetrics metrics = g.getFontMetrics(getFont());
            int totalScore = 0;

            Font orig = getFont();
            g.setFont(orig.deriveFont(18f));

            for (int i = 0; i < 4; i++) {
                int xx = x + (4 * (cardWidth + Constants.BOARD_CARD_SPACING * 2));
                int yy = y + (i * (cardHeight + Constants.BOARD_CARD_SPACING * 2));
//                Rectangle rectangle = new Rectangle(xx, yy, cardWidth, cardHeight);
                g.setColor(Color.white);
//                g.drawRect(xx, yy, cardWidth, cardHeight);
                totalScore += rowScores[i];
                String score = String.valueOf(rowScores[i]);
                g.drawString(score, xx + 12 - metrics.stringWidth(score) / 2, yy + cardHeight / 2);
            }

            for (int j = 0; j < 4; j++) {
                totalScore += columnScores[j];
                int xx = x + (j * (cardWidth + Constants.BOARD_CARD_SPACING * 2));
                int yy = y + (4 * (cardHeight + Constants.BOARD_CARD_SPACING * 2));
//                Rectangle rectangle = new Rectangle(xx, yy, cardWidth, cardHeight);
                g.setColor(Color.white);
//                g.drawRect(xx, yy, cardWidth, cardHeight);
                String score = String.valueOf(columnScores[j]);
                g.drawString(score, xx + cardWidth / 2 - metrics.stringWidth(score) / 2, yy + 14);
            }

            g.setFont(orig.deriveFont(Font.BOLD, 24f));
            g.setColor(Color.white);
            String score = String.valueOf(totalScore);
            int xx = x + (4 * (cardWidth + Constants.BOARD_CARD_SPACING * 2));
            int yy = y + (4 * (cardHeight + Constants.BOARD_CARD_SPACING * 2));
            int buffer = 18;
            g.drawString(score, xx + buffer - metrics.stringWidth(score) / 2, yy + buffer);
            g.setFont(orig);
        }

        private void drawTableBackground(Graphics g) {
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        }

        private void drawDeck(Graphics g) {
            final int spacing = Constants.BOARD_CARD_SPACING;

            Dimension cardDimension = getBoardCardDimension();
            int cardWidth = cardDimension.width;
            int cardHeight = cardDimension.height;

            int x = this.getWidth() / 6;
            int y = this.getHeight() / 5 - cardHeight - Constants.BOARD_CARD_SPACING * 2;

            deckRectangle = new Rectangle(x, y, cardWidth, cardHeight);

            if (starterCard == null) {
                ImageIcon icon = images.createScaledImageIconWithWhiteBackground("/images/png/kem-cardback.png", cardWidth, cardHeight);
                drawCard(g, icon, x, y, cardWidth, cardHeight);
            } else {
                ImageIcon icon = images.createScaledImageIconWithWhiteBackground(starterCard.getPngImagePath(), cardWidth, cardHeight);
                drawCard(g, icon, x, y, cardWidth, cardHeight);
            }
        }

        private void drawCurrentCard(Graphics g) {
            if (currentCard == null) {
                return;
            }

            final int spacing = Constants.BOARD_CARD_SPACING;

            Dimension cardDimension = getBoardCardDimension();
            int cardWidth = cardDimension.width;
            int cardHeight = cardDimension.height;

            int x = this.getWidth() / 6 + spacing * 2 + cardWidth;
            int y = this.getHeight() / 5 - cardHeight - Constants.BOARD_CARD_SPACING * 2;

            ImageIcon icon = images.createScaledImageIconWithWhiteBackground(currentCard.getPngImagePath(), cardWidth, cardHeight);
            drawCard(g, icon, x, y, cardWidth, cardHeight);
        }

        private void drawGrid(Graphics g) {
            Dimension cardDimension = getBoardCardDimension();
            int cardWidth = cardDimension.width;
            int cardHeight = cardDimension.height;

            int x = this.getWidth() / 6;
            int y = this.getHeight() / 5;

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    int xx = x + (j * (cardWidth + Constants.BOARD_CARD_SPACING * 2));
                    int yy = y + (i * (cardHeight + Constants.BOARD_CARD_SPACING * 2));
                    Rectangle rectangle = new Rectangle(xx, yy, cardWidth, cardHeight);
                    rectangles[i][j] = rectangle;
                    if (squares[i][j] == null) {
                        if (xHighlight == i && yHighlight == j) {
                            drawCardOutlineHighlighted(g, xx, yy, cardWidth, cardHeight);
                        } else {
                            drawCardOutline(g, xx, yy, cardWidth, cardHeight);
                        }
                    } else {
                        ImageIcon icon = images.createScaledImageIconWithWhiteBackground(squares[i][j].getPngImagePath(), cardWidth, cardHeight);
                        drawCard(g, icon, xx, yy, cardWidth, cardHeight);
                    }
                }
            }
        }

        private void drawCard(Graphics g, ImageIcon icon, int x, int y, int cardWidth, int cardHeight) {
            g.drawImage(icon.getImage(), x, y, this);
            g.setColor(Color.lightGray);
            g.drawRect(x, y, cardWidth, cardHeight);

            int shadow = 1;
            // Add a shadow to the bottom and right edges for the 3D effect
            g.setColor(new Color(150, 150, 150, 100)); // Shadow color with transparency
            g.fillRect(x + cardWidth, y + shadow, shadow, cardHeight); // Right shadow
            g.fillRect(x + shadow, y + cardHeight, cardWidth, shadow); // Bottom shadow

            // Add a highlight to the top and left edges for the 3D effect
            g.setColor(new Color(255, 255, 255, 100)); // Highlight color with transparency
            g.fillRect(x - shadow, y - shadow, cardWidth, shadow); // Top highlight
            g.fillRect(x - shadow, y - shadow, shadow, cardHeight); // Left highlight
        }

        private void drawCardOutline(Graphics g, int x, int y, int cardWidth, int cardHeight) {
            g.setColor(new Color(255, 255, 255, 32));
            g.fillRect(x, y, cardWidth, cardHeight);
            g.setColor(Color.lightGray);
            g.drawRect(x, y, cardWidth, cardHeight);
        }

        private void drawCardOutlineHighlighted(Graphics g, int x, int y, int cardWidth, int cardHeight) {
            g.setColor(new Color(0, 82, 204, 128));
            g.fillRect(x, y, cardWidth, cardHeight);
            g.setColor(Color.yellow);
            g.drawRect(x, y, cardWidth, cardHeight);
        }

        void deal() {
            if (currentCard != null) {
                setMessageText("Place current card before dealing next card.");
                repaint();
                return;
            }

            if (rabbitHunting && cards.isEmpty()) {
                // wrap around the deck for the starter card
                cards = new ArrayList<>(deck.shuffle());
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        cards.remove(squares[i][j]);
                    }
                }
            }

            if (cards.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Deck is empty.");
                return;
            }

            boolean full = true;
            boolean[][] available = new boolean[4][4];
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    available[i][j] = false;
                }
            }

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (squares[i][j] == null) {
                        full = false;
                        available[i][j] = true;
                    }
                }
            }

            if (full) {
                if (starterCard == null) {
                    starterCard = cards.remove(0);
                }
                computeScores();
                repaint();
                return;
            }

            currentCard = cards.remove(0);

            validate();
            repaint();
        }

        private void computeScores() {
            // rows
            rowScores = new int[4];
            for (int i = 0; i < 4; i++) {
                Card[] handCards = new Card[4];
                for (int j = 0; j < 4; j++) {
                    handCards[j] = squares[i][j];
                }
                CribbageHand hand = new CribbageHand(handCards, starterCard);
                rowScores[i] = hand.scoreHand();
            }

            // columns
            columnScores = new int[4];
            for (int j = 0; j < 4; j++) {
                Card[] handCards = new Card[4];
                for (int i = 0; i < 4; i++) {
                    handCards[i] = squares[i][j];
                }
                CribbageHand hand = new CribbageHand(handCards, starterCard);
                columnScores[j] = hand.scoreHand();
            }
        }

        private boolean isGridFull() {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (squares[i][j] == null) {
                        return false;
                    }
                }
            }
            return true;
        }

        private void setMessageText(String s) {
            int x = 20;
            int y = getHeight() - 20;
            getGraphics().setColor(Color.WHITE);
            getGraphics().drawString(s, x, y);
        }

        void newGame() {
            cards = new ArrayList<>(deck.shuffle());
            rowScores = null;
            columnScores = null;
            squares = new Card[4][4];
            currentCard = null;
            starterCard = null;

            setFocusable(true); // needed for key listener
            requestFocusInWindow();

            deal();
            validate();
            repaint();
        }

        private Dimension getBoardCardDimension() {
            double cardWidth = this.getHeight() / 10;
            double cardHeight = cardWidth / Constants.CARD_RATIO;
            return new Dimension((int) Math.round(cardWidth), (int) Math.round(cardHeight));
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (deckRectangle != null && deckRectangle.contains(e.getX(), e.getY())) {
                deal();
                return;
            }

            if (currentCard != null) {
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        if (rectangles[i][j] != null && rectangles[i][j].contains(e.getX(), e.getY())) {
                            squares[i][j] = currentCard;
                            currentCard = null;
                            deal();
                            repaint();
                            return;
                        }
                    }
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}