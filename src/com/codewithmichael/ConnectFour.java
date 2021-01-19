package com.codewithmichael;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class ConnectFour {

    // some variables
    public static final int HEIGHT = 6;
    public static final int WIDTH = 15;
    public static final char PLAYER_ONE = 'X';
    public static final char PLAYER_TWO = 'O';

    public static void main(String[] args) {

        System.out.println("Connect Four - Java Implementation By Michael Roddy");
        System.out.println(" ");

        // createBoard() returns 2d char array
        char[][] board = createBoard();

        printBoard(board);

        Scanner input = new Scanner(System.in);

        int numPlayers;
        /*
        take input for the number of players.
        loop until the correct number of payers (1 or 2) is entered
         */
        System.out.println("How many players? Enter 1 or 2: ");
        while (true) {
            try {
                numPlayers = input.nextInt();
                input.nextLine(); // handles next line
                if (numPlayers >= 1 && numPlayers <= 2) {
                    break;
                } else {
                    System.out.println("Please enter 1 or 2.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error, please enter a number.");
                input.nextLine();
            }
        }

        String playerTurn; // holds which players turn it is
        String sideUp = flipCoin(); // flipCoin() returns heads or tails, assign to sideUp

        /*
        Determines which player goes first
        loop until player 1 enters either heads or tails
        if player 1 enters heads or tails and if sideUp == player 1's choice,
        then player 1 goes first otherwise, player 2 goes first.
         */
        System.out.println("Who goes first? Lets flip a coin.");
        System.out.println("Player 1 enter heads or tails: ");
        while (true) {
            String choice = input.nextLine();
            if (choice.equals("heads") || choice.equals("tails")) {
                if (choice.equals(sideUp)) {
                    System.out.println("Yay! Coin flipped " + sideUp + ". You go first.");
                    System.out.println(" ");
                    playerTurn = "player1";
                } else {
                    System.out.println("Unlucky! Coin flipped " + sideUp + ". Player two goes first.");
                    System.out.println(" ");
                    playerTurn = "player2";
                }
                break;
            } else {
                System.out.println("Invalid input. \nPlease enter heads or tails: ");
            }
        }

        /*
        if numPlayers == 1, player 1 will play against the computer.
        loop as long the board is not full, take user input for player,
        check if the column entered by the user is a valid move, if it is,
        make that move, otherwise, print error message.
        then, check for win, if the player has won, print the winning message and
        break out of the loop, otherwise playerTurn = player2.
         */
        if (numPlayers == 1) {
            while (boardIsNotFull(board)) {
                if (playerTurn.equals("player1")) { // player 1 goes
                    try {
                        System.out.println("Player 1 enter column (1-7): ");
                        int column = input.nextInt();
                        int convertedColumnNumber = convertColumnNumber(column);
                        if (isValidMove(board, convertedColumnNumber)) {
                            makePlayer1Move(board, convertedColumnNumber);
                            if (checkWin(board)) {
                                printBoard(board);
                                System.out.println("Hurray! You won.");
                                break;
                            }
                            playerTurn = "player2";
                        } else {
                            System.out.println("Not a valid move, try again.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Error, please enter a number.");
                        input.nextLine();
                    }
                } else { // computer goes
                    computerBestMove(board);
                    if (checkWin(board)) {
                        printBoard(board);
                        System.out.println("Hard luck! Computer won.");
                        break;
                    }
                    printBoard(board);
                    playerTurn = "player1";
                }
            }
        /*
        otherwise, if number of players == 2, player 1 will play against player 2.
        loop as along as the board is not full and take input alternating between
        player 1 and player 2.
        check if the column entered by the user is a valid move, if it is,
        make that move, otherwise, print error message.
        then, check for win, if the player has won, print the winning message and
        break out of the loop, otherwise switch player turn and start again.
         */
        } else { // otherwise player 1 will play against player 2
            while (boardIsNotFull(board)) {
                if (playerTurn.equals("player1")) { // players 1 goes
                    try {
                        System.out.println("Player 1 enter column (1-7): ");
                        int column = input.nextInt();
                        int convertedColumnNumber = convertColumnNumber(column);
                        if (isValidMove(board, convertedColumnNumber)) {
                            makePlayer1Move(board, convertedColumnNumber);
                            if (checkWin(board)) {
                                printBoard(board);
                                System.out.println("Hurray! Player 1 won.");
                                break;
                            }
                            printBoard(board);
                            playerTurn = "player2";
                        } else {
                            System.out.println("Not a valid move, try again.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Error, please enter a number.");
                        input.nextLine();
                    }
                } else { // player 2 goes
                    try {
                        System.out.println("Player 2 enter column (1-7): ");
                        int column = input.nextInt();
                        int convertedColumnNumber = convertColumnNumber(column);
                        if (isValidMove(board, convertedColumnNumber)) {
                            makePlayer2Move(board, convertedColumnNumber);
                            if (checkWin(board)) {
                                printBoard(board);
                                System.out.println("Hurray! Player two won.");
                                break;
                            }
                            printBoard(board);
                            playerTurn = "player1";
                        } else {
                            System.out.println("Not a valid move, try again.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Error, please enter a number.");
                        input.nextLine();
                    }
                }
            }
        }
    }

    /*
    creates and returns game board as 2d char array
     */
    public static char[][] createBoard() {
        char[][] board = new char[HEIGHT][WIDTH];
        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                if (col % 2 == 0) {
                    board[row][col] = '|';
                } else {
                    board[row][col] = '_';
                }
            }
        }
        return board;
    }

    /*
    takes the current state of the game board as a parameter and loops
    through the board printing its contents.
     */
    public static void printBoard(char[][] board) {
        // print column numbers
        for (int i = 0; i < 7; i++) {
            System.out.print(" " + (i+1));
        }
        System.out.println(" ");

        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                System.out.print(board[row][col]);
            }
            System.out.println();
        }
        System.out.println("---------------");
    }

    /*
    converts the column number entered by user e.g 1, 2, 3 etc...
    to corresponding column number in the 2d array.
    e.g 1 = 1, 2 = 3, 3 = 5, 4 = 7, 5 = 9 etc...
     */
    public static int convertColumnNumber(int column) {
        return column + (column - 1);
    }

    /*
    takes a column as a parameter and starts checking from the bottom row
    if the row/column is empty, place player 1 piece.
     */
    public static void makePlayer1Move(char[][] board, int column) {
        for (int row = HEIGHT-1; row >= 0; row--) {
            if (board[row][column] == '_') {
                board[row][column] = PLAYER_ONE;
                break;
            }
        }
    }

    /*
    takes a column as a parameter and starts checking from the bottom row
    if the row/column is empty, place player 2 piece.
     */
    public static void makePlayer2Move(char[][] board, int column) {
        for (int row = HEIGHT-1; row >= 0; row--) {
            if (board[row][column] == '_') {
                board[row][column] = PLAYER_TWO;
                break;
            }
        }
    }

    public static void undoLastMove(char[][] board, int column) {
        for (int row = 0; row < HEIGHT; row++) {
            if (board[row][column] == PLAYER_ONE || board[row][column] == PLAYER_TWO) {
                board[row][column] = '_';
                break;
            }
        }
    }

    /*
    a move is valid if the column entered is not less than or greater than the board width
    and a move is valid if the column is not full.
     */
    public static boolean isValidMove(char[][] board, int column) {
        return column > 0 && column < 15 && board[0][column] == '_';
    }

    /*
    if the top row of the game board is not empty, then the board is full.
     */
    public static boolean boardIsNotFull(char[][] board) {
        int count = 0;
        // check if row 0 is full
        for (int col = 1; col < WIDTH-1; col += 2) {
            if (board[0][col] != '_') {
                count++;
            }
        }
        return count != 7;
    }

    /*
    generates a random number between 1 and 2 inclusive.
    if random number == 1, return heads, otherwise return tails.
     */
    public static String flipCoin() {
        Random rand = new Random();
        int randomNum = rand.nextInt(2);
        if (randomNum == 1) {
            return "heads";
        }
        else {
            return "tails";
        }
    }

    /*
    method check for win on the horizontal, vertical, and diagonals.
    returns true if there is 4 in a row.
     */
    public static boolean checkWin(char[][] board) {
        //check horizontal
        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH-8; col += 2) {
                if (board[row][col+1] != '_'
                        && board[row][col+1] == board[row][col+3]
                        && board[row][col+3] == board[row][col+5]
                        && board[row][col+5] == board[row][col+7]) {
                    return true;
                }
            }
        }

        // check vertical
        for (int col = 0; col < WIDTH-2; col += 2) {
            for (int row = 0; row < HEIGHT-3; row++) {
                if (board[row][col+1] != '_'
                        && board[row][col+1] == board[row+1][col+1]
                        && board[row+1][col+1] == board[row+2][col+1]
                        && board[row+2][col+1] == board[row+3][col+1]) {
                    return true;
                }
            }
        }

        // check diagonal top left to bottom right
        for (int row = 0; row < HEIGHT-3; row++) {
            for (int col = 0; col < WIDTH-8; col += 2) {
                if (board[row][col+1] != '_'
                        && board[row][col+1] == board[row+1][col+3]
                        && board[row+1][col+3] == board[row+2][col+5]
                        && board[row+2][col+5] == board[row+3][col+7]) {
                    return true;
                }
            }
        }

        // check diagonal top right to bottom left
        for (int row = 0; row < HEIGHT-3; row++) {
            for (int col = WIDTH-1; col >= WIDTH-7; col -= 2) {
                if (board[row][col-1] != '_'
                        && board[row][col-1] == board[row+1][col-3]
                        && board[row+1][col-3] == board[row+2][col-5]
                        && board[row+2][col-5] == board[row+3][col-7]) {
                    return true;
                }
            }
        }

        return false; // otherwise return false

    }

    /*
    minimax considers all the possible game states and returns the best score
     */
    public static int minimax(char[][] board, int depth, boolean isMaximiser) {

        int staticEvaluation = boardEvaluation(board);

        // if terminating state return static evaluation of the position
        if (depth == 0 || checkWin(board) || !boardIsNotFull(board)) {
            return staticEvaluation;
        }

        int bestScore;

        if (isMaximiser) { // if maximiser's move

            bestScore = Integer.MIN_VALUE;

            for (int col = 1; col < WIDTH-1; col += 2) {
                if (isValidMove(board, col)) {
                    makePlayer2Move(board, col);
                    int score = minimax(board, depth+1, false);
                    bestScore = Math.max(bestScore, score);
                    undoLastMove(board, col);
                }
            }

        } else { // if minimiser's move

            bestScore = Integer.MAX_VALUE;

            for (int col = 1; col < WIDTH-1; col += 2) {
                if (isValidMove(board, col)) {
                    makePlayer1Move(board, col);
                    int score = minimax(board, depth+1, true);
                    bestScore = Math.min(score, bestScore);
                    undoLastMove(board, col);
                }
            }

        }
        return bestScore;

    }

    /*
    loops through valid columns and calls minimax,
    if the current score is greater than bestScore,
    then this is the best move the maximiser can make
    so make that move.
     */
    public static void computerBestMove(char[][] board) {

        int optimalCol = 0;
        int bestScore  = Integer.MIN_VALUE;

        for (int col = 1; col < WIDTH-1; col += 2) {
            if (isValidMove(board, col)) {
                makePlayer2Move(board, col);
                int score = minimax(board, 0, true);
                if (score > bestScore) {
                    bestScore = score;
                    optimalCol = col; // best column for computer
                }
                undoLastMove(board, col);
            }
        }
        makePlayer2Move(board, optimalCol);
    }

    /*
    this method evaluates the current state of the game board.
    a value is given to each state of the game. this value is
    computed by how good it would be for a player to reach that position.
    - 1000000 points if the agent has 4 pieces in a row
    - 1 point if the agent filled three spots, and the remaining spot is empty
    (the agent wins if it fills in the empty spot)
    - -100 points if the opponent filled three spots, and the remaining spot is empty
    (the opponent wins by filling in the empty spot).
     */
    public static int boardEvaluation(char[][] board) {
        int score = 0;
        //check horizontal
        for (int row = HEIGHT-1; row >= 0; row--) {
            for (int col = 1; col < WIDTH-7; col += 2) {
                if (board[row][col] == PLAYER_ONE
                        && board[row][col] == board[row][col+2]
                        && board[row][col+2] == board[row][col+4]
                        && board[row][col+6] == '_') {
                    score += -100;
                }
                if (board[row][col] == PLAYER_ONE
                        && board[row][col] == board[row][col+2]
                        && board[row][col+4] == '_'
                        && board[row][col+6] == board[row][col]) {
                    score += -100;
                }
                if (board[row][col] == PLAYER_ONE
                        && board[row][col+2] == '_'
                        && board[row][col+4] == board[row][col]
                        && board[row][col+6] == board[row][col]) {
                    score += -100;
                }
                if (board[row][col] == '_'
                        && board[row][col+2] == PLAYER_ONE
                        && board[row][col+2] == board[row][col+4]
                        && board[row][col+4] == board[row][col+6]) {
                    score += -100;
                }
                if (board[row][col] == '_'
                        && board[row][col+2] == PLAYER_ONE
                        && board[row][col+4] == PLAYER_ONE
                        && board[row][col+6] == '_') {
                    score += -100;
                }
                if (board[row][col] == PLAYER_TWO
                        && board[row][col] == board[row][col+2]
                        && board[row][col+2] == board[row][col+4]
                        && board[row][col+6] == '_') {
                    score += 1;
                }
                if (board[row][col] == PLAYER_TWO
                        && board[row][col] == board[row][col+2]
                        && board[row][col+4] == '_'
                        && board[row][col+6] == board[row][col]) {
                    score += 1;
                }
                if (board[row][col] == PLAYER_TWO
                        && board[row][col+2] == '_'
                        && board[row][col+4] == board[row][col]
                        && board[row][col+6] == board[row][col]) {
                    score += 1;
                }
                if (board[row][col] == '_'
                        && board[row][col+2] == PLAYER_TWO
                        && board[row][col+2] == board[row][col+4]
                        && board[row][col+4] == board[row][col+6]) {
                    score += 1;
                }
                if (board[row][col] == PLAYER_TWO
                        && board[row][col] == board[row][col+2]
                        && board[row][col+2] == board[row][col+4]
                        && board[row][col+4] == board[row][col+6]) {
                    score += 1000000;
                }
            }
        }

        // check vertical
        for (int col = 1; col < WIDTH-1; col += 2) {
            for (int row = HEIGHT-1; row >= HEIGHT-3; row--) {
                if (board[row][col] == PLAYER_ONE
                        && board[row][col] == board[row-1][col]
                        && board[row-1][col] == board[row-2][col]
                        && board[row-3][col] == '_') {
                    score += -100;
                }
                if (board[row][col] == PLAYER_ONE
                        && board[row][col] == board[row-1][col]
                        && board[row-2][col] == '_'
                        && board[row-3][col] == board[row][col]) {
                    score += -100;
                }
                if (board[row][col] == PLAYER_ONE
                        && board[row-1][col] == '_'
                        && board[row-2][col] == board[row][col]
                        && board[row-3][col] == board[row][col]) {
                    score += -100;
                }
                if (board[row][col] == '_'
                        && board[row-1][col] == PLAYER_ONE
                        && board[row-1][col] == board[row-2][col]
                        && board[row-2][col] == board[row-3][col]) {
                    score += -100;
                }
                if (board[row][col] == PLAYER_TWO
                        && board[row][col] == board[row-1][col]
                        && board[row-1][col] == board[row-2][col]
                        && board[row-3][col] == '_') {
                    score += 1;
                }
                if (board[row][col] == PLAYER_TWO
                        && board[row][col] == board[row-1][col]
                        && board[row-2][col] == '_'
                        && board[row-3][col] == board[row][col]) {
                    score += 1;
                }
                if (board[row][col] == PLAYER_TWO
                        && board[row-1][col] == '_'
                        && board[row-2][col] == board[row][col]
                        && board[row-3][col] == board[row][col]) {
                    score += 1;
                }
                if (board[row][col] == '_'
                        && board[row-1][col] == PLAYER_TWO
                        && board[row-1][col] == board[row-2][col]
                        && board[row-2][col] == board[row-3][col]) {
                    score += 1;
                }
                if (board[row][col] == PLAYER_TWO
                        && board[row][col] == board[row-1][col]
                        && board[row-1][col] == board[row-2][col]
                        && board[row-2][col] == board[row-3][col]) {
                    score += 1000000;
                }
            }
        }

        // check diagonal top left to bottom right
        for (int row = 0; row < HEIGHT-3; row++) {
            for (int col = 1; col < WIDTH-7; col += 2) {
                if (board[row][col] == PLAYER_ONE
                        && board[row][col] == board[row+1][col+2]
                        && board[row+1][col+2] == board[row+2][col+4]
                        && board[row+3][col+6] == '_') {
                    score += -100;
                }
                if (board[row][col] == PLAYER_ONE
                        && board[row][col] == board[row+1][col+2]
                        && board[row+2][col+4] == '_'
                        && board[row+3][col+6] == board[row][col]) {
                    score += -100;
                }
                if (board[row][col] == PLAYER_ONE
                        && board[row+1][col+2] == '_'
                        && board[row+2][col+4] == board[row][col]
                        && board[row+3][col+6] == board[row][col]) {
                    score += -100;
                }
                if (board[row][col] == '_'
                        && board[row+1][col+2] == PLAYER_ONE
                        && board[row+1][col+2] == board[row+2][col+4]
                        && board[row+2][col+4] == board[row+3][col+6]) {
                    score += -100;
                }
                if (board[row][col] == PLAYER_TWO
                        && board[row][col] == board[row+1][col+2]
                        && board[row+1][col+2] == board[row+2][col+4]
                        && board[row+3][col+6] == '_') {
                    score += 1;
                }
                if (board[row][col] == PLAYER_TWO
                        && board[row][col] == board[row+1][col+2]
                        && board[row+2][col+4] == '_'
                        && board[row+3][col+6] == board[row][col]) {
                    score += 1;
                }
                if (board[row][col] == PLAYER_TWO
                        && board[row+1][col+2] == '_'
                        && board[row+2][col+4] == board[row][col]
                        && board[row+3][col+6] == board[row][col]) {
                    score += 1;
                }
                if (board[row][col] == '_'
                        && board[row+1][col+2] != '_'
                        && board[row+1][col+2] == board[row+2][col+4]
                        && board[row+2][col+4] == board[row+3][col+6]) {
                    score += 1;
                }
                if (board[row][col] == PLAYER_TWO
                        && board[row][col] == board[row+1][col+2]
                        && board[row+1][col+2] == board[row+2][col+4]
                        && board[row+2][col+4] == board[row+3][col+6]) {
                    score += 1000000;
                }
            }
        }

        // check diagonal top right to bottom left
        for (int row = 0; row < HEIGHT-3; row++) {
            for (int col = WIDTH-2; col >= 7; col -= 2) {
                if (board[row][col] == PLAYER_ONE
                        && board[row][col] == board[row+1][col-2]
                        && board[row+1][col-2] == board[row+2][col-4]
                        && board[row+3][col-6] == '_') {
                    score += -100;
                }
                if (board[row][col] == PLAYER_ONE
                        && board[row][col] == board[row+1][col-2]
                        && board[row+2][col-4] == '_'
                        && board[row+3][col-6] == board[row][col]) {
                    score += -100;
                }
                if (board[row][col] == PLAYER_ONE
                        && board[row+1][col-2] == '_'
                        && board[row+2][col-4] == board[row][col]
                        && board[row+3][col-6] == board[row][col]) {
                    score += -100;
                }
                if (board[row][col] == '_'
                        && board[row+1][col-2] == PLAYER_ONE
                        && board[row+1][col-2] == board[row+2][col-4]
                        && board[row+2][col-4] == board[row+3][col-6]) {
                    score += -100;
                }
                if (board[row][col] == PLAYER_TWO
                        && board[row][col] == board[row+1][col-2]
                        && board[row+1][col-2] == board[row+2][col-4]
                        && board[row+3][col-6] == '_') {
                    score += 1;
                }
                if (board[row][col] == PLAYER_TWO
                        && board[row][col] == board[row+1][col-2]
                        && board[row+2][col-4] == '_'
                        && board[row+3][col-6] == board[row][col]) {
                    score += 1;
                }
                if (board[row][col] == PLAYER_TWO
                        && board[row+1][col-2] == '_'
                        && board[row+2][col-4] == board[row][col]
                        && board[row+3][col-6] == board[row][col]) {
                    score += 1;
                }
                if (board[row][col] == '_'
                        && board[row+1][col-2] == PLAYER_TWO
                        && board[row+1][col-2] == board[row+2][col-4]
                        && board[row+2][col-4] == board[row+3][col-6]) {
                    score += 1;
                }
                if (board[row][col] == PLAYER_TWO
                        && board[row][col] == board[row+1][col-2]
                        && board[row+1][col-2] == board[row+2][col-4]
                        && board[row+2][col-4] == board[row+3][col-6]) {
                    score += 1000000;
                }
            }
        }
        return score;
    }
}

