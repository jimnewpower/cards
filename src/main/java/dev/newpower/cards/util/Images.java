package dev.newpower.cards.util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Images {

    public ImageIcon createCardback(int width, int height) {
        final String path = "/images/png/kem-cardback.png";

        try {
            BufferedImage originalImage = ImageIO.read(getClass().getResource(path));

            // Create a new image with a white background
            BufferedImage whiteBackgroundImage = new BufferedImage(
                    originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = whiteBackgroundImage.createGraphics();
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, originalImage.getWidth(), originalImage.getHeight());
            g2d.drawImage(originalImage, 0, 0, null);
            g2d.dispose();

            // Scale the image with a white background
            Image scaledImage = whiteBackgroundImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public ImageIcon createScaledImageIconWithWhiteBackground(String path, int width, int height) {
        try {
            BufferedImage originalImage = ImageIO.read(getClass().getResource(path));

            // Create a new image with a white background
            BufferedImage whiteBackgroundImage = new BufferedImage(
                    originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = whiteBackgroundImage.createGraphics();
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, originalImage.getWidth(), originalImage.getHeight());
            g2d.drawImage(originalImage, 0, 0, null);
            g2d.dispose();

            // Scale the image with a white background
            Image scaledImage = whiteBackgroundImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ImageIcon createScaledImageIconWithGrayBackground(String path, int width, int height) {
        try {
            BufferedImage originalImage = ImageIO.read(getClass().getResource(path));

            // Create a new image with a white background
            BufferedImage whiteBackgroundImage = new BufferedImage(
                    originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = whiteBackgroundImage.createGraphics();
            g2d.setColor(Color.GRAY);
            g2d.fillRect(0, 0, originalImage.getWidth(), originalImage.getHeight());
            g2d.drawImage(originalImage, 0, 0, null);
            g2d.dispose();

            // Scale the image with a white background
            Image scaledImage = whiteBackgroundImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ImageIcon createScaledImageIcon(String path, int width, int height) {
        try {
            BufferedImage img = ImageIO.read(getClass().getResource(path));
            Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImg);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ImageIcon createImageIcon(String path) {
        try {
            BufferedImage originalImage = ImageIO.read(getClass().getResource(path));

            // Create a new image with a white background
            BufferedImage whiteBackgroundImage = new BufferedImage(
                    originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = whiteBackgroundImage.createGraphics();
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, originalImage.getWidth(), originalImage.getHeight());
            g2d.drawImage(originalImage, 0, 0, null);
            g2d.dispose();
            return new ImageIcon(originalImage);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
