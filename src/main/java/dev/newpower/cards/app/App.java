package dev.newpower.cards.app;

import dev.newpower.cards.games.MainView;
import dev.newpower.cards.games.shuffler.ShuffleView;

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
            MainView mainView = new MainView();
            mainView.setTitle("Shuffle");
            mainView.setSize(600, 360);
            mainView.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            mainView.setVisible(true);
        });
    }
}
