package org.cis1200.twentyfortyeight;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Run2048 implements Runnable {
    public void run() {
        // NOTE: the 'final' keyword denotes immutability even for local variables.

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("2048");
        frame.setLocation(300, 300);

        // Get score bc it's not always going to be 0
        int initialScore = 0;

        try {
            BufferedReader reader = new BufferedReader(
                    new FileReader("src/main/java/org/cis1200/twentyfortyeight/state.txt")
            );
            initialScore = Integer.parseInt(reader.readLine());
        } catch (IOException e) {
            System.out.println("State file not found");
        }

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("2048");
        status_panel.add(status);
        final JLabel score = new JLabel("Score: " + initialScore);
        status_panel.add(score);

        // Game board
        final GameBoard board = new GameBoard(status, score);
        frame.add(board, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        String instructs = "Hello and welcome to my 2048 game!" +
                "To play, use the WASD keys. W moves all the tiles "
                + "\n up, S moves them down, A moves them to the left,"
                + " and D moves them to the right. Tiles with the "
                + "\n same number merge into one tile with the sum"
                + " of the two numbers. The game ends when you win by "
                + "\n getting the 2048 tile or lose by having no new"
                + " moves. Every time you move, a tile with value 2 "
                + "\n or 4 randomly spawns in. There is an undo"
                + " button to undo your previous move (note that your "
                + "\n score will be adjusted accordingly) and"
                + " a reset button to restart the game. Note that you can't"
                + "\n undo after you've already lost"
                + " (as that would be unfair) so be careful. Have fun!";

        // Note here that when we add an action listener to the reset button, we
        // define it as an anonymous inner class that is an instance of
        // ActionListener with its actionPerformed() method overridden. When the
        // button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> board.reset());
        final JButton undo = new JButton("Undo");
        undo.addActionListener(e -> board.undo());
        final JButton instructions = new JButton("Instructions");
        instructions.addActionListener(
                e -> JOptionPane.showMessageDialog(null, instructs, "Instructions", 1)
        );
        instructions.addActionListener(
                e -> board.focus()
        );
        control_panel.add(instructions);
        control_panel.add(reset);
        control_panel.add(undo);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Focus on board
        board.focus();

    }
}
