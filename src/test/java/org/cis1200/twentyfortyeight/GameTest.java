package org.cis1200.twentyfortyeight;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    private TwentyFortyEight game;

    private final int[][] testBoard = {
        { 2, 0, 2, 2 },
        { 2, 0, 0, 2 },
        { 4, 4, 0, 8 },
        { 2, 2, 4, 4 }
    };

    // This tests checkEnd to see if the game is won (there is a 2048 tile)
    @Test
    public void testCheckEndWon() {
        game = new TwentyFortyEight();
        int[][] board = {
            { 2048, 32, 0, 0 },
            { 8, 16, 4, 0 },
            { 4, 2, 0, 4 },
            { 16, 2, 4, 0 }
        };
        game.setState(board);
        assertEquals(2, game.checkEnd());
        game.reset();
    }

    // This tests checkEnd to see if the game is lost (no more moves left)
    @Test
    public void testCheckEndLost() {
        game = new TwentyFortyEight();
        int[][] board = {
            { 2, 4, 2, 4 },
            { 4, 2, 4, 2 },
            { 2, 4, 2, 4 },
            { 4, 2, 4, 2 }
        };

        game.setState(board);
        assertEquals(1, game.checkEnd());
        game.reset();
    }

    // This tests checkEnd to see if the game is still going (moves left and not
    // won)
    @Test
    public void testCheckEndActive() {
        game = new TwentyFortyEight();
        int[][] board = {
                {2, 2, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };

        game.setState(board);
        assertEquals(-1, game.checkEnd());
        game.reset();
    }
}
