package ss.gamedesign;

import ss.exceptions.InvalidMoveException;
import ss.utils.TextIO;

import java.util.Scanner;

public class Game {

    /**
     * An array of the players.
     *
     * @invariant the length of the array equals 2
     * @invariant all array items are never null
     */
    private Player[] players;

    /**
     * The board for the game.
     *
     * @invariant board is never null
     */
    private Board board;

    /**
     * The player at turn.
     */
    private int current;

    /**
     * An instance of the move class.
     */
    private Move move;

    public Game(Player player1, Player player2) {
        players = new Player[]{player1, player2};
        current = (int) (Math.random() * 2);
        board = new Board();
        move = new Move(board);
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    private void updateBoardLayout() {
        System.out.println("\nCurrent game situation: \n\n" + board.toString());
    }

    private void resetGame() {
        current = (int) (Math.random() * 2);
        board.initialiseBoard();
    }

    private void playGame() {
        Scanner sc = new Scanner(System.in);


        while (!board.gameOver(move)) {

            if (!move.noPossibleSingleMoves()) {
                System.out.println("Single move required: ");
                int playerMove = sc.nextInt();
                move.singleMove(players[current], playerMove);
            } else if (!move.noPossibleDoubleMoves()) {
                System.out.println("Double move required: ");
                int playerMove1 = sc.nextInt();
                sc.nextLine();
                int playerMove2 = sc.nextInt();
                move.doubleMove(players[current], playerMove1, playerMove2);
            }

            if (current == 0) {
                current = 1;
            } else {
                current = 0;
            }
            updateBoardLayout();
        }
    }

    private boolean continuePlaying() {
        String answer = TextIO.getlnString();
        if (answer.equals("y")) {
            board.initialiseBoard();
            return true;
        } else if (answer.equals("n")) {
            return false;
        } else {
            System.out.println("Please enter valid input.");
            return continuePlaying();
        }
    }

    public void startGame() throws InvalidMoveException {
        boolean continueGame = true;
        while (continueGame) {
            resetGame();
            System.out.println(board.toString());
            playGame();
            updateBoardLayout();
            players[0].setPoints();
            players[1].setPoints();
            players[0].setNumberOfBalls();
            players[1].setNumberOfBalls();
            System.out.println(players[0].getName() + " has " + players[0].getPoints() + " points and " + players[0].getNumberOfBalls() + " balls");
            System.out.println(players[1].getName() + " has " + players[1].getPoints() + " points and " + players[1].getNumberOfBalls() + " balls");
            System.out.println("\nPlay again? (y/n)");
            continueGame = continuePlaying();
        }
    }
}
