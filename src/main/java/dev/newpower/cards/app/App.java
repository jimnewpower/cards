package dev.newpower.cards.app;

import dev.newpower.cards.controller.ShuffleView;

import javax.swing.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater(() -> {
            JButton riffle = new JButton("Riffle");
            JButton cut = new JButton("Cut");
            JButton shuffle = new JButton("Simple Shuffle");
            JButton fullShuffle = new JButton("Full Shuffle");
            JButton reset = new JButton("Reset");
            ShuffleView view = new ShuffleView(riffle, cut, shuffle, fullShuffle, reset);
            riffle.addActionListener(e1 -> view.riffle());
            cut.addActionListener(e1 -> view.cut());
            shuffle.addActionListener(e1 -> view.shuffleSimple());
            fullShuffle.addActionListener(e1 -> view.shuffle());
            reset.addActionListener(e1 -> view.resetDeck());
        });
    }
}
