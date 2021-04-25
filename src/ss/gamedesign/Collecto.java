package ss.gamedesign;

import ss.exceptions.InvalidMoveException;

public class Collecto {

    public static void main(String[] args) throws InvalidMoveException {

        Player player1 = new HumanPlayer("Dimitri");
        Player player2 = new HumanPlayer("Vanio");
        Game game = new Game(player1, player2);
//        System.out.println(game.getBoard().getField());
        game.startGame();

//        Board board = new Board();
//        Move move = new Move(board);
//
//        System.out.println(board.toString() + "\n");
//        move.fillSingleMovesMap();
//        System.out.println(board.toString() + "\n");

    }
}
