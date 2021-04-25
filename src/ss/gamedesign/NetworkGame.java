package ss.gamedesign;

import ss.exceptions.InvalidMoveException;
import ss.exceptions.JoinGameException;
import ss.server.CollectoClientHandler;

import java.util.Collection;
import java.util.HashMap;

/**
 * Class for maintaining the Collecto game.
 * @author Yordan Tsintsov and Yunchen Sun
 */
public class NetworkGame {

    /**
     * The number of players.
     * @invariant Always has to be 2
     */
    private int numberOfPlayers;

    /**
     * The board.
     * @invariant board is never null
     */
    private Board board;

    public void setBoard(Board board) {
		this.board = board;
	}

	/**
     * The movements reference object.
     */
    public Move move;

    public void setMove(Move move) {
		this.move = move;
	}

	/**
     * The 2 players of the game.
     * @invariant the length of the array equals numberOfPlayers
     * @invariant all array items are never null
     */
    private Player[] players;

    /**
     * Index of the current player.
     * @invariant the index is always between 0 and numberOfPlayers
     */
    private int current;

    /**
     * Number of players joined.
     * @invariant the index is always between 0 and numberOfPlayers
     */
    private int joinedPlayers;

    /**
     * Number of players that are ready.
     * @invariant the index is always between 0 and numberOfPlayers
     */
    private int readyPlayers;

    /**
     * Game password.
     * @invariant the password is not hashed
     */
    private String name;

    /**
     * Map containing the playerHandlers as values and their names as keys.
     */
    private HashMap<String, CollectoClientHandler> playerHandlers;

    /**
     * Construct a NetworkGame.
     * @param gameName name of the game
     * @param capacity the capacity of the game
     */
    public NetworkGame(String gameName, int capacity) {
        board = new Board();
        move = new Move(board);
        players = new Player[capacity];
        numberOfPlayers = 2;
        //numberOfPlayers = 1;
        name = gameName;
        current = (int) (Math.random() * 2);
        joinedPlayers = 0;
        readyPlayers = 0;
        playerHandlers = new HashMap<>();
    }

    /**
     * Called when a player joins the game. Adds the player and their handler to the game.
     * @param playerName name of the player
     * @param handler handler for the player
     * @throws JoinGameException thrown when the game is full
     */
    public synchronized void join(String playerName, CollectoClientHandler handler)  throws JoinGameException {

        if (joinedPlayers == numberOfPlayers) {
            throw new JoinGameException("The game is full. Please join another game.");
        }

        for (int i = 0; i < players.length; i++) {
            if (players[i] == null) {
                players[i] = new HumanPlayer(playerName);
                playerHandlers.put(playerName, handler);
                handler.joinActiveGame(this);
                joinedPlayers++;
                // Exit if the player was joined
                break;
            }
        }

        if (joinedPlayers == numberOfPlayers) {
            if (numberOfPlayers == 2) {
                board.initialiseBoard();
            }
        }
    }

    // -- Commands --------------------------------------------------------------

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public int getJoinedPlayers() {
        return joinedPlayers;
    }

    public void setJoinedPlayers(int joinedPlayers) {
        this.joinedPlayers = joinedPlayers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Resets the game. <br>
     * The board is emptied and player[0] becomes the current player.
     */
    public void reset() {
        current = (int) (Math.random() * 2);
        board.initialiseBoard();
        joinedPlayers = 0;
        playerHandlers.clear();

    }

    public Board getBoard() {
        return board;
    }

    /**
     * Returns a Collection with all client handlers of an active game.
     * @return Collection with all client handlers
     */
    public Collection<CollectoClientHandler> getClientHandlers() {
        return this.playerHandlers.values();
    }

    public CollectoClientHandler getNextTurnClientHandler() {
        // Get next turn player handler from the hashmap by current player name
        //return this.playerHandlers.get(players[current].getName());
    	return this.playerHandlers.get("b");
    }

    /**
     * Called when a player signifies they are ready to start the game. Increments the number of ready players.
     * @param username the username of the player
     */
    public synchronized void ready(String username) {
        if (readyPlayers < numberOfPlayers) {
            readyPlayers++;
        }
    }

    public int getReadyPlayers() {
        return readyPlayers;
    }

    public Move getMove() {
        return move;
    }

    /**
     * Returns a boolean that indicates whether the game has ended or not.
     * @return true if the game is over, false otherwise
     */
    public boolean isOver() {
        return board.gameOver(move);
    }

    /**
     * Checks which player has more points and which more balls
     * and returns the name of the winner.
     * @return the name of the winner of the game
     */
    public String getWinner() {
        String winner = null;

        if (players[0].getPoints() == players[1].getPoints()) {
            if (players[0].getNumberOfBalls() > players[1].getNumberOfBalls()) {
                winner = players[0].getName();
            } else if (players[0].getNumberOfBalls() < players[1].getNumberOfBalls()) {
                winner = players[1].getName();
            }
        } else if (players[0].getPoints() > players[1].getPoints()) {
            winner = players[0].getName();
        } else {
            winner = players[1].getName();
        }
        return winner;
    }

    /**
     * Checks if the players have the same points and the same number of balls
     * and shows if the game was a draw.
     * @return true if the players have the same number of points and balls
     */
    public boolean isDraw() {
        if (players[0].getPoints() == players[1].getPoints()) {
            return players[0].getNumberOfBalls() == players[1].getNumberOfBalls();
        }
        return false;
    }

    /**
     * Redirects the move to the board.
     * @param moves the move to be made
     * @throws InvalidMoveException thrown when the move is invalid
     */
    public void move(String[] moves) throws InvalidMoveException {
        try {
            Player currentPlayer = players[current];

            if (!move.noPossibleSingleMoves() && moves.length == 1) {
                move.singleMove(currentPlayer, Integer.parseInt(moves[0]));
            } else if (moves.length == 2) {
                move.doubleMove(currentPlayer, Integer.parseInt(moves[0]), Integer.parseInt(moves[1]));
            } else {
                throw new InvalidMoveException("Invalid arguments");
            }

            if (current == numberOfPlayers - 1) {
                current = 0;
            } else {
                current++;
            }

        } catch (InvalidMoveException e) {
            throw e;
        }
    }

}
