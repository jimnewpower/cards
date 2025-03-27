package dev.newpower.cards.app;

import dev.newpower.cards.games.MainView;

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
            mainView.setTitle("Cards");
            mainView.setSize(600, 360);
            mainView.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            mainView.setIconImage(new ImageIcon(mainView.getClass().getResource("/images/png/bicycle-back.png")).getImage());
            mainView.setVisible(true);
        });
    }
}
