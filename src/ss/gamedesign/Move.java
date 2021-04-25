package ss.gamedesign;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Move {

    /**
     * The board of the game.
     */
    private Board board;

    public void setBoard(Board board) {
		this.board = board;
	}

	/**
     * A copy of the board used to play the game. It is used
     * to reverse any changes to the board that result from illegal moves.
     */
    private Board copy;

    /**
     * The dimensions of the board.
     */
    private static final int DIM = 7;

    /**
     * An array list that contains all the indexes of balls with
     * neighbours of the same colour.
     */
    private ArrayList<Integer> sameNeighboursArrayList;

    /**
     * A map that contains all possible and impossible moves.
     */
    private Map<Integer, Boolean> singleMoves;

    /**
     * Initializes the Move class with the board from class Board.
     * @param board the board value assigned to the instance variable
     */
    public Move(Board board) {
        this.board = board;
    	this.copy = board.deepCopy();
        sameNeighboursArrayList = new ArrayList<>();
        singleMoves = new HashMap<>();
        fillSingleMovesMap();
    }

    // -- ROW CONTROL --------------------------------------------------------

    /**
     * Checks if there are empty fields in the row corresponding to row.
     * @param row the index of the row
     * @return true if there is a row with an empty field
     */
    public boolean rowCanBeMoved(int row) {
        for (int j = 0; j < 7; j++) {
            if (board.getField(row, j).getBall().getColour() == Colour.EMPTY) {
                return true;
            }
        }
        return false;
    }

    // -- RIGHT ROW CONTROL

    /**
     * Checks if the values in the row corresponding to parameter row have an empty neighbour
     * on their right.
     * @param row the row of the board
     * @return true if a field on row row has an empty neighbour on their right
     */
    public boolean hasEmptyRightNeighbour(int row) {
        for (int j = 0; j < DIM - 1; j++) {
            if (board.getField(row, j).getBall().getColour() != Colour.EMPTY && board.getField(row, j + 1).getBall().getColour() == Colour.EMPTY) {
                return true;
            }
        }
        return false;
    }

    /**
     * Moves all the non-empty fields to the right.
     * @param row the row of the board
     */
    public void moveRowOnRight(int row) {
        if (rowCanBeMoved(row)) {
            while (hasEmptyRightNeighbour(row)) {
                for (int j = 0; j < DIM - 1; j++) {
                    if (board.getField(row, j + 1).getBall().getColour() == Colour.EMPTY) {
                        board.setField(board.getField(row, j).getBall().getColour(), row, j + 1);
                        board.setField(Colour.EMPTY, row, j);
                    }
                }
            }
        }
    }

    // -- LEFT ROW CONTROL

    /**
     * Checks if the values in the row corresponding to parameter row have an empty neighbour
     * on their left.
     * @requires row >= 0 && row < 7
     * @param row the row of the board
     * @return true if a field on row with index row has an empty neighbour on their right
     */
    public boolean hasEmptyLeftNeighbour(int row) {
        for (int j = 1; j < DIM; j++) {
            if (board.getField(row, j).getBall().getColour() != Colour.EMPTY && board.getField(row, j - 1).getBall().getColour() == Colour.EMPTY) {
                return true;
            }
        }
        return false;
    }

    /**
     * Moves all the non-empty fields to the left.
     * @requires row >= 0 && row < 7
     * @param row the row of the board
     */
    public void moveRowOnLeft(int row) {
        if (rowCanBeMoved(row)) {
            while (hasEmptyLeftNeighbour(row)) {
                for (int j = 1; j < DIM; j++) {
                    if (board.getField(row, j - 1).getBall().getColour() == Colour.EMPTY) {
                        board.setField(board.getField(row, j).getBall().getColour(), row, j - 1);
                        board.setField(Colour.EMPTY, row, j);
                    }
                }
            }
        }
    }

    // -- COLUMN CONTROL ----------------------------------------------------------------------

    /**
     * Checks if there is an empty field in the selected column.
     * @param j the column of the board
     * @requires j >= 0 && j < 7
     * @ensures we can move the column up or down
     * @return true if there is an empty field in the selected column
     */
    public boolean columnCanBeMoved(int j) {
        for (int i = 0; i < DIM; i++) {
            if (board.getField(i, j).getBall().getColour() == Colour.EMPTY) {
                return true;
            }
        }
        return false;
    }

    // UPPER COLUMN CONTROL

    /**
     * Checks if the values in the column corresponding to parameter column have an empty neighbour
     * beneath them.
     * @requires column >= 0 && column < 7
     * @param column the column of the board
     * @return true if a field on column with index column has an empty neighbour beneath them
     */
    public boolean hasEmptyLowerNeighbour(int column) {
        for (int i = 0; i < DIM - 1; i++) {
            if (board.getField(i, column).getBall().getColour() != Colour.EMPTY && board.getField(i + 1, column).getBall().getColour() == Colour.EMPTY) {
                return true;
            }
        }
        return false;
    }

    /**
     * Moves all the non-empty fields down.
     * @requires column >= 0 && column < 7
     * @param column the column of the board
     */
    public void moveColumnDown(int column) {
        if (columnCanBeMoved(column)) {
            while (hasEmptyLowerNeighbour(column)) {
                for (int i = 0; i < DIM - 1; i++) {
                    if (board.getField(i + 1, column).getBall().getColour() == Colour.EMPTY) {
                        board.setField(board.getField(i, column).getBall().getColour(), i + 1, column);
                        board.setField(Colour.EMPTY, i, column);
                    }
                }
            }
        }
    }

    // -- LOWER COLUMN CONTROL

    /**
     * Checks if the values in the column corresponding to parameter column have an empty neighbour
     * above them.
     * @requires column >= 0 && column < 7
     * @param column the column of the board
     * @return true if a field on column with index column has an empty neighbour above them
     */
    public boolean hasEmptyUpperNeighbour(int column) {
        for (int i = 1; i < DIM; i++) {
            if (board.getField(i, column).getBall().getColour() != Colour.EMPTY && board.getField(i - 1, column).getBall().getColour() == Colour.EMPTY) {
                return true;
            }
        }
        return false;
    }

    /**
     * Moves all the non-empty fields up.
     * @requires column >= 0 && column < 7
     * @param column the column of the board
     */
    public void moveColumnUp(int column) {
        if (columnCanBeMoved(column)) {
            while (hasEmptyUpperNeighbour(column)) {
                for (int i = 1; i < DIM; i++) {
                    if (board.getField(i - 1, column).getBall().getColour() == Colour.EMPTY) {
                        board.setField(board.getField(i, column).getBall().getColour(), i - 1, column);
                        board.setField(Colour.EMPTY, i, column);
                    }
                }
            }
        }
    }

    // -- WHOLE BOARD CONTROL -----------------------------------------------------------------

    /**
     * Fills the array list with the indexes of the field and its neighbours
     * that are of the same colour.
     * @requires row >= 0 && row < DIM && column >= 0 && column < DIM
     * @param row the row of the field
     * @param col the column of the field
     */
    public void fillArrayList(int row, int col) {
        if (board.hasSameNeighbours(row, col) && !board.isEmptyField(row, col)) {
            for (int i = 0; i < board.getNeighboursIndex(row, col).length; i++) {
                if (board.getNeighboursIndex(row, col)[i] != -1 && board.ballHasSameColour(board.getField(row, col), board.getField(board.getNeighboursIndex(row, col)[i]))) {
                    if (!sameNeighboursArrayList.contains(board.getField(board.getNeighboursIndex(row, col)[i]).getIndex())) {
                        sameNeighboursArrayList.add(board.getField(board.getNeighboursIndex(row, col)[i]).getIndex());
                    }
                }
            }
            if (!sameNeighboursArrayList.contains(board.getField(row, col).getIndex())) {
                sameNeighboursArrayList.add(board.getField(row, col).getIndex());
            }
        }
    }

    /**
     * Fills the array list with the indexes of the field and its neighbours
     * that are of the same colour for every field of the board.
     */
    public void fillArrayList() {
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                fillArrayList(i, j);
            }
        }
    }

    /**
     * Sets all the balls in the fields of the board with neighbours of the same colour
     * to Colour.EMPTY.
     */
    public void removeSameBalls() {
        for (Integer integer : sameNeighboursArrayList) {
            board.setField(Colour.EMPTY, integer);
        }
        sameNeighboursArrayList.clear();
    }

    /**
     * Checks if the arrayList size is different from zero in order to know if currently there
     * are any balls on the board that are neighbours and have the same colour.
     * @return true if the arrayList has a value different than zero
     */
    public boolean isMoveValid() {
        return sameNeighboursArrayList.size() != 0;
    }

    /**
     * Updates all the values of the copy of the board
     * to the values of the board.
     * @ensures copy.fields[i][j] = board.fields[i][j]
     */
    public void updateCopy() {
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                copy.setField(board.getField(i, j).getBall().getColour(), i, j);
            }
        }
    }

    /**
     * Updates all the values of the board to the values
     * of the copy of the original board.
     * @ensures board.fields[i][j] = copy.fields[i][j]
     */
    public void reverseMove() {
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                board.setField(copy.getField(i, j).getBall().getColour(), i, j);
            }
        }
    }

    /**
     * Set up for the commands used to move rows and columns in a specific direction.
     * Every commands is equivalent to a moveRow or moveCol method in a specific direction.
     * @param command the command used in the game to move rows or columns
     * @requires command >= 0 && command <= 27
     */
    private void commands(int command) {
        switch (command) {
            case 0:
                moveRowOnLeft(0);
                break;

            case 1:
                moveRowOnLeft(1);
                break;

            case 2:
                moveRowOnLeft(2);
                break;

            case 3:
                moveRowOnLeft(3);
                break;

            case 4:
                moveRowOnLeft(4);
                break;

            case 5:
                moveRowOnLeft(5);
                break;

            case 6:
                moveRowOnLeft(6);
                break;
            case 7:
                moveRowOnRight(0);
                break;

            case 8:
                moveRowOnRight(1);
                break;

            case 9:
                moveRowOnRight(2);
                break;

            case 10:
                moveRowOnRight(3);
                break;

            case 11:
                moveRowOnRight(4);
                break;

            case 12:
                moveRowOnRight(5);
                break;

            case 13:
                moveRowOnRight(6);
                break;

            case 14:
                moveColumnUp(0);
                break;

            case 15:
                moveColumnUp(1);
                break;

            case 16:
                moveColumnUp(2);
                break;

            case 17:
                moveColumnUp(3);
                break;

            case 18:
                moveColumnUp(4);
                break;

            case 19:
                moveColumnUp(5);
                break;

            case 20:
                moveColumnUp(6);
                break;

            case 21:
                moveColumnDown(0);
                break;

            case 22:
                moveColumnDown(1);
                break;

            case 23:
                moveColumnDown(2);
                break;

            case 24:
                moveColumnDown(3);
                break;

            case 25:
                moveColumnDown(4);
                break;

            case 26:
                moveColumnDown(5);
                break;

            case 27:
                moveColumnDown(6);
                break;
        }
    }

    /**
     * Fill the map with all the possible single moves as keys
     * and set their value to true of false depending on whether
     * the move is possible or not.
     */
    public void fillSingleMovesMap() {
        for (int i = 0; i < 28; i++) {
            commands(i);
            fillArrayList();
            if (isMoveValid()) {
                singleMoves.put(i, true);
            } else {
                singleMoves.put(i, false);
            }
            sameNeighboursArrayList.clear();
            reverseMove();
        }
    }

    /**
     * Goes through the singleMoves map and checks if there is a
     * single move left for the players to use.
     * @return true if there are no possible single moves for the board
     */
    public boolean noPossibleSingleMoves() {
        for (boolean command : singleMoves.values()) {
            if (command) {
                return false;
            }
        }
        return true;
    }

    /**
     * Make a single move.
     * The method first checks if a single move can be performed. If it can
     * it makes the move, sets the balls to be removed to the player performing
     * the move and then removes the balls from the board. Then it updates the
     * board copy and fills the map with the new possible single moves.
     * @requires command >= 0 && command <= 27
     * @ensures no invalid moves change the board
     * @param command the command used to move rows or columns that should
     *                gather balls from the same colour
     */
    public void singleMove(Player player, int command) {
        if (singleMoves.get(command)) {
            commands(command);
            fillArrayList();
            takeBalls(player);
            removeSameBalls();
            updateCopy();
            fillSingleMovesMap();
        }
    }

    /**
     * Make a double move.
     * Make two movements on the board, then check if the move was valid.
     * If the move was valid set the balls to be removed to the player
     * performing the move and then remove those balls from the board.
     * Update the copy and the single moves map.
     * @requires command1 >= 0 && command1 <= 27
     * @requires command2 >= 0 && command2 <= 27
     * @param command1 the first move used to move a row or a column
     * @param command2 the second move that should gather balls from the same colour
     */
    public void doubleMove(Player player, int command1, int command2) {
        commands(command1);
        commands(command2);
        fillArrayList();
        if (isMoveValid()) {
            takeBalls(player);
            removeSameBalls();
            updateCopy();
        } else {
            reverseMove();
            fillArrayList();
        }
        fillSingleMovesMap();
    }

    /**
     * Checks if any double moves can be performed if single moves cannot
     * be performed.
     * @ensures when can double moves be performed
     * @return true if no single moves can be performed and there exists a valid double move
     */
    public boolean noPossibleDoubleMoves() {
        if (noPossibleSingleMoves()) {
            for (int i = 0; i < 28; i++) {
                for (int j = 0; j < 28; j++) {
                    commands(i);
                    commands(j);
                    fillArrayList();
                    if (isMoveValid()) {
                        reverseMove();
                        fillArrayList();
                        return false;
                    }
                    reverseMove();
                    fillArrayList();
                }
            }
        }
        return true;
    }

    public Board getBoard() {
        return board;
    }

    public ArrayList<Integer> getSameNeighboursArrayList() {
        return sameNeighboursArrayList;
    }

    public Map<Integer, Boolean> getSingleMoves() {
        return singleMoves;
    }

    /**
     * Fills the player's map with the balls he has removed from the board.
     * @param player the player that is at turn
     */
    public void takeBalls(Player player) {
        for (Integer integer : sameNeighboursArrayList) {
            player.increasePlayerBalls(board.getField(integer).getBall().getColour());
            player.fillPlayerBalls();
        }
    }

}
