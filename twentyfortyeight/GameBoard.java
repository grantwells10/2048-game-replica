package org.cis1200.twentyfortyeight;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameBoard extends JPanel {

    private TwentyFortyEight game;
    public static final int BOARD_WIDTH = 525;
    public static final int BOARD_HEIGHT = 525;

    private JLabel gameStatus;
    private JLabel gameScore;

    GameBoard(JLabel status, JLabel score) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setFocusable(true);
        gameStatus = status;
        gameScore = score;
        game = new TwentyFortyEight();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (game.isActive()) {
                    if (e.getKeyChar() == 'd') {
                        game.moveRight();
                        addNew();
                    } else if (e.getKeyChar() == 'a') {
                        game.moveLeft();
                        addNew();
                    } else if (e.getKeyChar() == 's') {
                        game.moveDown();
                        addNew();
                    } else if (e.getKeyChar() == 'w') {
                        game.moveUp();
                        addNew();
                    }
                    updateScore();
                    updateStatus();
                    repaint();
                }
            }
        });

    }

    public void addNew() {
        if (game.countZeros() != 0) {
            game.addNumber();
        }
        game.save();
    }

    public void updateScore() {
        gameScore.setText("Score: " + game.getScore());
    }

    public void reset() {
        game.reset();
        gameScore.setText("Score: " + 0);
        gameStatus.setText("2048");
        repaint();
        focus();
    }

    public void focus() {
        requestFocusInWindow();
    }

    public void undo() {
        game.undo();
        gameScore.setText("Score: " + game.getScore());
        repaint();
        focus();
    }

    public void updateStatus() {
        int active = game.checkEnd();
        if (active == 1) {
            gameStatus.setText("You lose!!");
        } else if (active == 2) {
            gameStatus.setText("You win!!");
        }
    }

    /**
     * Draws the game board.
     *
     * There are many ways to draw a game board. This approach
     * will not be sufficient for most games, because it is not
     * modular. All of the logic for drawing the game board is
     * in this method, and it does not take advantage of helper
     * methods. Consider breaking up your paintComponent logic
     * into multiple methods or classes, like Mushroom of Doom.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw 4x4 Grid
        g.setColor(new Color(164, 147, 129));
        g.fillRect(0, 0, 500, 25);
        g.fillRect(0, 125, 500, 25);
        g.fillRect(0, 250, 500, 25);
        g.fillRect(0, 375, 500, 25);
        g.fillRect(0, 500, 500, 25);
        g.fillRect(0, 0, 25, 525);
        g.fillRect(125, 0, 25, 500);
        g.fillRect(250, 0, 25, 500);
        g.fillRect(375, 0, 25, 500);
        g.fillRect(500, 0, 25, 525);

        // Tiles
        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 36));
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int state = game.getCell(i, j);
                int y = i * (100) + 25 * (i + 1);
                int x = j * (100) + 25 * (j + 1);
                if (state == 0) {
                    g.setColor(new Color(190, 175, 160));
                    g.fillRect(x, y, 100, 100);
                } else if (state == 2) {
                    g.setColor(new Color(238, 228, 218));
                    g.fillRect(x, y, 100, 100);
                    g.setColor(new Color(119, 110, 101));
                    g.drawString("2", x + 40, y + 60);
                } else if (state == 4) {
                    g.setColor(new Color(237, 224, 200));
                    g.fillRect(x, y, 100, 100);
                    g.setColor(new Color(119, 110, 101));
                    g.drawString("4", x + 40, y + 60);
                } else if (state == 8) {
                    g.setColor(new Color(242, 177, 121));
                    g.fillRect(x, y, 100, 100);
                    g.setColor(new Color(255, 255, 255));
                    g.drawString("8", x + 40, y + 60);
                } else if (state == 16) {
                    g.setColor(new Color(245, 149, 99));
                    g.fillRect(x, y, 100, 100);
                    g.setColor(new Color(255, 255, 255));
                    g.drawString("16", x + 30, y + 60);
                } else if (state == 32) {
                    g.setColor(new Color(246, 124, 96));
                    g.fillRect(x, y, 100, 100);
                    g.setColor(new Color(255, 255, 255));
                    g.drawString("32", x + 30, y + 60);
                } else if (state == 64) {
                    g.setColor(new Color(246, 94, 59));
                    g.fillRect(x, y, 100, 100);
                    g.setColor(new Color(255, 255, 255));
                    g.drawString("64", x + 30, y + 60);
                } else if (state == 128) {
                    g.setColor(new Color(237, 207, 115));
                    g.fillRect(x, y, 100, 100);
                    g.setColor(new Color(255, 255, 255));
                    g.drawString("128", x + 22, y + 60);
                } else if (state == 256) {
                    g.setColor(new Color(237, 204, 98));
                    g.fillRect(x, y, 100, 100);
                    g.setColor(new Color(255, 255, 255));
                    g.drawString("256", x + 20, y + 60);
                } else if (state == 512) {
                    g.setColor(new Color(237, 200, 80));
                    g.fillRect(x, y, 100, 100);
                    g.setColor(new Color(255, 255, 255));
                    g.drawString("512", x + 20, y + 60);
                } else if (state == 1024) {
                    g.setColor(new Color(237, 197, 63));
                    g.fillRect(x, y, 100, 100);
                    g.setColor(new Color(255, 255, 255));
                    g.drawString("1024", x + 10, y + 60);
                } else if (state == 2048) {
                    g.setColor(new Color(237, 194, 45));
                    g.fillRect(x, y, 100, 100);
                    g.setColor(new Color(255, 255, 255));
                    g.drawString("2048", x + 10, y + 60);
                }
            }
        }
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }

}
