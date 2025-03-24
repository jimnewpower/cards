package dev.newpower.cards.games;

import dev.newpower.cards.games.cribbage.squares.GameView;
import dev.newpower.cards.games.shuffler.ShuffleView;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {

    public MainView() {
        setLayout(new FlowLayout());

        AbstractButton shuffleButton = new JButton("Shuffle...");
        add(shuffleButton);
        shuffleButton.addActionListener(ae -> {
            JButton riffle = new JButton("Riffle");
            JButton cut = new JButton("Cut");
            JButton shuffle = new JButton("Fisher-Yates Shuffle");
            JButton fullShuffle = new JButton("Multi-Combo Shuffle");
            JButton reset = new JButton("Reset");
            ShuffleView view = new ShuffleView(riffle, cut, shuffle, fullShuffle, reset);
            riffle.addActionListener(e1 -> view.riffle());
            cut.addActionListener(e1 -> view.cut());
            shuffle.addActionListener(e1 -> view.shuffleSimple());
            fullShuffle.addActionListener(e1 -> view.shuffle());
            reset.addActionListener(e1 -> view.resetDeck());
            view.setVisible(true);
        });

        AbstractButton cribbageSquaresButton = new JButton("Cribbage Squares...");
        add(cribbageSquaresButton);
        cribbageSquaresButton.addActionListener(ae -> {
            GameView gameView = new GameView();
        });
    }
}
