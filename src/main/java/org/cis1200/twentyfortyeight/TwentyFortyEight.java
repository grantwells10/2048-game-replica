package org.cis1200.twentyfortyeight;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;

public class TwentyFortyEight {

    private int[][] board;
    private int score;
    private static LinkedList<int[][]> history;

    private boolean gameActive;
    private static LinkedList<Integer> scores;

    public TwentyFortyEight() {
        board = new int[4][4];
        read();
        gameActive = true;
        history = new LinkedList<>();
        scores = new LinkedList<>();
        history.add(board);
        scores.add(score);
    }

    public void reset() {
        board = new int[4][4];
        score = 0;
        history = new LinkedList<>();
        scores = new LinkedList<>();
        gameActive = true;
        addNumber();
        addNumber();
        history.add(board);
        save();
        scores.add(0);
    }

    public void undo() {
        if (history.size() > 1 && gameActive) {
            scores.removeLast();
            history.removeLast();
            score = scores.getLast();
            int[][] lastState = copyState(history.getLast());
            board = lastState;
            save();
        }
    }

    public void save() {
        try {
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter("src/main/java/org/cis1200/twentyfortyeight/state.txt")
            );
            writer.write("" + score + "\n");
            for (int[] row : board) {
                int counter = 0;
                for (int element : row) {
                    if (counter == 3) {
                        writer.write("" + element);
                    } else {
                        writer.write("" + element + " ");
                    }
                    counter++;
                }
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing the file");
        }
    }

    public void read() {
        try {
            BufferedReader reader = new BufferedReader(
                    new FileReader("src/main/java/org/cis1200/twentyfortyeight/state.txt")
            );
            score = Integer.parseInt(reader.readLine());
            for (int i = 0; i < 4; i++) {
                String[] row = reader.readLine().split(" ");
                for (int j = 0; j < 4; j++) {
                    board[i][j] = Integer.parseInt(row[j]);
                }
            }
        } catch (IOException e) {
            System.out.println("State file doesn't exist");
        }
    }

    public int[] pickIndex() {
        int x = (int) (Math.random() * 4);
        int y = (int) (Math.random() * 4);
        while (board[y][x] != 0) {
            x = (int) (Math.random() * 4);
            y = (int) (Math.random() * 4);
        }
        int[] pos = { x, y };
        return pos;
    }

    // this function randomly adds a new number 2 or 4 to the grid (4 has a lower
    // chance of being added)
    public void addNumber() {
        int random = (int) (Math.random() * 3) + 1;
        int[] position = pickIndex();
        int x = position[0];
        int y = position[1];
        if (random == 1 || random == 2) {
            board[y][x] = 2;
        } else {
            board[y][x] = 4;
        }

    }

    public int countZeros() {
        int counter = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == 0) {
                    counter++;
                }
            }
        }
        return counter;
    }

    // this functions rotates the grid by 90 degrees
    public void rotate() {
        int[][] result = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[i][j] = board[3 - j][i];
            }
        }
        board = result;
    }

    // this is a helper function that moves everything downward and merges any two
    // of the same tile
    public void move() {
        for (int i = 0; i < 4; i++) {
            // 2 0 0 0
            if (board[i][1] == 0 && board[i][2] == 0 && board[i][3] == 0) {
                board[i][3] = board[i][0];
                board[i][0] = 0;
            } else if (board[i][0] == 0 && board[i][2] == 0 && board[i][3] == 0) {
                // 0 2 0 0
                board[i][3] = board[i][1];
                board[i][1] = 0;
            } else if (board[i][0] == 0 && board[i][1] == 0 && board[i][3] == 0) {
                // 0 0 2 0
                board[i][3] = board[i][2];
                board[i][2] = 0;
            } else if (board[i][0] != 0 && board[i][1] != 0
                    && board[i][2] == 0 && board[i][3] == 0) {
                // 2 2 0 0
                if (board[i][0] == board[i][1]) {
                    board[i][3] = board[i][0] + board[i][1];
                    score += board[i][3];
                } else {
                    board[i][3] = board[i][1];
                    board[i][2] = board[i][0];
                }
                board[i][1] = 0;
                board[i][0] = 0;
            } else if (board[i][1] == 0 && board[i][3] == 0
                    && board[i][0] != 0 && board[i][2] != 0) {
                // 2 0 2 0
                if (board[i][0] == board[i][2]) {
                    board[i][3] = board[i][0] + board[i][2];
                    score += board[i][3];
                    board[i][2] = 0;
                } else {
                    board[i][3] = board[i][2];
                    board[i][2] = board[i][0];
                }
                board[i][1] = 0;
                board[i][0] = 0;
            } else if (board[i][0] != 0 && board[i][1] == 0
                    && board[i][2] == 0 && board[i][3] != 0) {
                // 2 0 0 2
                if (board[i][3] == board[i][0]) {
                    board[i][3] += board[i][0];
                    score += board[i][3];
                    board[i][2] = 0;
                } else {
                    board[i][2] = board[i][0];
                }
                board[i][1] = 0;
                board[i][0] = 0;
            } else if (board[i][1] != 0 && board[i][3] != 0
                    && board[i][0] == 0 && board[i][2] == 0) {
                // 0 2 0 2
                if (board[i][3] == board[i][1]) {
                    board[i][3] += board[i][1];
                    board[i][2] = 0;
                    score += board[i][3];
                } else {
                    board[i][2] = board[i][1];
                }
                board[i][1] = 0;
                board[i][0] = 0;
            } else if (board[i][0] == 0 && board[i][1] != 0
                    && board[i][2] != 0 && board[i][3] == 0) {
                // 0 2 2 0
                if (board[i][1] == board[i][2]) {
                    board[i][3] = board[i][1] + board[i][2];
                    board[i][2] = 0;
                    score += board[i][3];
                } else {
                    board[i][3] = board[i][2];
                    board[i][2] = board[i][1];
                }
                board[i][1] = 0;
            } else if (board[i][0] == 0 && board[i][1] == 0
                    && board[i][2] != 0 && board[i][3] != 0) {
                // 0 0 2 2
                if (board[i][2] == board[i][3]) {
                    board[i][3] += board[i][2];
                    score += board[i][3];
                    board[i][2] = 0;
                }
            } else if (board[i][3] == 0) {
                // 2 2 2 0
                if (board[i][2] == board[i][1]) {
                    board[i][3] = board[i][2] + board[i][1];
                    board[i][2] = board[i][0];
                    board[i][1] = 0;
                    score += board[i][3];
                } else if (board[i][1] == board[i][0]) {
                    board[i][3] = board[i][2];
                    board[i][2] = board[i][0] + board[i][1];
                    score += board[i][2];
                    board[i][1] = 0;
                } else {
                    board[i][3] = board[i][2];
                    board[i][2] = board[i][1];
                    board[i][1] = board[i][0];
                }
                board[i][0] = 0;

            } else if (board[i][0] == 0) {
                // 0 2 2 2
                if (board[i][3] == board[i][2]) {
                    board[i][3] = board[i][2] + board[i][3];
                    board[i][2] = board[i][1];
                    board[i][1] = 0;
                    score += board[i][3];
                } else if (board[i][1] == board[i][2]) {
                    board[i][2] = board[i][1] + board[i][2];
                    board[i][1] = 0;
                    score += board[i][2];
                }
            } else if (board[i][1] == 0) {
                // 2 0 2 2
                if (board[i][3] == board[i][2]) {
                    board[i][3] += board[i][2];
                    board[i][2] = board[i][0];
                    score += board[i][3];
                } else if (board[i][0] == board[i][2]) {
                    board[i][2] += board[i][0];
                    score += board[i][2];
                } else {
                    board[i][1] = board[i][0];
                }
                board[i][0] = 0;
            } else if (board[i][2] == 0) {
                // 2 2 0 2
                if (board[i][3] == board[i][1]) {
                    board[i][3] += board[i][1];
                    board[i][2] = board[i][0];
                    board[i][1] = 0;
                    score += board[i][3];
                } else if (board[i][0] == board[i][1]) {
                    board[i][2] = board[i][0] + board[i][1];
                    board[i][1] = 0;
                    score += board[i][2];
                } else {
                    board[i][2] = board[i][1];
                    board[i][1] = board[i][0];
                }
                board[i][0] = 0;
            } else {
                // 2 2 2 2
                if (board[i][3] == board[i][2]) {
                    board[i][3] += board[i][2];
                    score += board[i][3];
                    if (board[i][0] == board[i][1]) {
                        board[i][2] = board[i][0] + board[i][1];
                        board[i][1] = 0;
                        score += board[i][2];
                    } else {
                        board[i][2] = board[i][1];
                        board[i][1] = board[i][0];
                    }
                    board[i][0] = 0;
                } else if (board[i][2] == board[i][1]) {
                    board[i][2] += board[i][1];
                    board[i][1] = board[i][0];
                    board[i][0] = 0;
                    score += board[i][2];
                } else if (board[i][0] == board[i][1]) {
                    board[i][1] += board[i][0];
                    board[i][0] = 0;
                    score += board[i][1];
                }
            }
        }
        scores.add(score);
    }

    // this function moves everything down and merges any necessary blocks
    public void moveRight() {
        // ENCAPSULATION: move() touches board directly so make sure its not attached to
        // anything else first
        board = copyState(board);
        move();
        save();
        history.add(board);
    }

    // this function moves everything left and merges any necessary blocks
    public void moveUp() {
        rotate();
        move();
        rotate();
        rotate();
        rotate();
        save();
        history.add(board);
    }

    // this function moves everything right and merges any necessary blocks
    public void moveDown() {
        rotate();
        rotate();
        rotate();
        move();
        rotate();
        save();
        history.add(board);
    }

    // this function moves everything up and merges any necessary blocks
    public void moveLeft() {
        rotate();
        rotate();
        move();
        rotate();
        rotate();
        save();
        history.add(board);
    }

    public boolean equals(int[][] arr1, int[][] arr2) {
        return (Arrays.equals(arr1[0], arr2[0]) && Arrays.equals(arr1[1], arr2[1])
                && Arrays.equals(arr1[2], arr2[2]) && Arrays.equals(arr1[3], arr2[3]));
    }

    // this function checks the status of the game. 1 is returned if you lose. 2 is
    // returned if you win. -1 if neither
    public int checkEnd() {
        // check to see if the game is won (there is a 2048)
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == 2048) {
                    gameActive = false;
                    return 2;
                }
            }
        }
        // check to see if there are no moves left
        if (countZeros() == 0) {
            int[][] currentState = copyState(board);
            moveDown();
            if (!equals(board, currentState)) {
                board = copyState(currentState);
                return -1;
            }
            board = copyState(currentState);
            moveUp();
            if (!equals(board, currentState)) {
                board = copyState(currentState);
                return -1;
            }
            board = copyState(currentState);
            moveRight();
            if (!equals(board, currentState)) {
                board = copyState(currentState);
                return -1;
            }
            board = copyState(currentState);
            moveLeft();
            if (!equals(board, currentState)) {
                board = copyState(currentState);
                return -1;
            }
            gameActive = false;
            return 1;
        }
        return -1;

    }

    // makes a copy of the given array for encapsulation purposes
    public int[][] copyState(int[][] arr) {
        int[][] copy = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                copy[i][j] = arr[i][j];
            }
        }
        return copy;
    }

    public int getCell(int i, int j) {
        return board[i][j];
    }

    public int getScore() {
        return this.score;
    }

    // USE FOR TESTING

    public int[][] getState() {
        return copyState(this.board);
    }

    public boolean isActive() {
        return this.gameActive;
    }

    public void setState(int[][] newBoard) {
        board = copyState(newBoard);
        save();
    }

}
