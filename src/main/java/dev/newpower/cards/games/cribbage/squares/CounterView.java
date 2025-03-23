package dev.newpower.cards.games.cribbage.squares;

import javax.swing.*;
import java.awt.*;

public class CounterView extends JFrame {
    private final JPanel panel;

    public CounterView() {
        setTitle("Cribbage Counter");
        setSize(1290, 624);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setIconImage(new ImageIcon(getClass().getResource("/images/png/poker_chip_100.png")).getImage());
        setLayout(new BorderLayout());
        this.panel = new JPanel(new BorderLayout());
    }
}
