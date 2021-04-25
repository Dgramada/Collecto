package ss.gamedesign;

import java.util.HashMap;
import java.util.Map;

public class Board {

    /**
     * The board dimensions size.
     */
    private static final int DIM = 7;

    /**
     * The line splitting rows.
     */
    private static final String LINE = "+----+----+----+----+----+----+----+----+----+";

    /**
     * The column indexes representing the possible commands.
     */
    private static final String TOP_COLUMNS = "     | 21 | 22 | 23 | 24 | 25 | 26 | 27 |";
    private static final String BOTTOM_COLUMNS = "     | 14 | 15 | 16 | 17 | 18 | 19 | 20 |";

    /**
     * A double array that represents the fields from the board.
     */
    private final Field[][] fields;

    /**
     * A map that uses the 6 colours as keys and stores
     * the number of balls that are missing from the board
     * to their corresponding key.
     */
    private final Map<Colour, Integer> colourCounter;

    /**
     * The counters showing the number of the balls
     * for every colour that are not on the board.
     */
    private int blueCounter = 8;
    private int redCounter = 8;
    private int orangeCounter = 8;
    private int greenCounter = 8;
    private int yellowCounter = 8;
    private int purpleCounter = 8;

    /**
     * Initialises instance variable fields[][].
     * Creates an empty board and then fills it with random balls
     * with 8 balls of every one of the six colors.
     * Sets the center field to be empty.
     */
    public Board() {
        colourCounter = new HashMap<>();
        fields = new Field[7][7];
        initialiseBoard();

    }

    /**
     *
     */
    public void initialiseBoard() {
        int breakPoint = 0;
        boolean boardNotReady = true;
        fillMap();

        while (boardNotReady) { // Fill/refill the board until it is created correctly

            setBoardFieldsToEmpty(); // Set the fields to empty
            setColourCounterValuesToMax(); // Set the counters to 8

            Task:
            for (int i = 0; i < 7; i++) { // Iterate through the rows
                for (int j = 0; j < 7; j++) { // Iterate through the columns
                    if (fields[i][j] != fields[3][3]) {
                        fields[i][j].getBall().setRandomColour();
                        subtractCount(fields[i][j]);
                        while (hasSameNeighbours(i, j) || getColourCounter(fields[i][j]) < 0) { // Change the ball value until it is appropriate for the field

                            if (hasSameNeighbours(i, j)) { // Check if the field has different neighbours
                                increaseCount(fields[i][j]);
                                fields[i][j].getBall().setRandomColour();
                                subtractCount(fields[i][j]);
                            }

                            if (getColourCounter(fields[i][j]) < 0) { // Check if there are balls of this type left to be put on the board
                                fields[i][j].getBall().setRandomColour();
                                subtractCount(fields[i][j]);
                            }

                            breakPoint++;
                            if (breakPoint == 1000) { // Cancel the for loops if breakpoint reaches 1000
                                breakPoint = 0;
                                fields[i][j].getBall().setColour(Colour.EMPTY);
                                break Task;
                            }
                        }
                    }
                }
            }

            if (boardIsReady(fields)) {
                boardNotReady = false;
            }
            setColourCounterValuesToZero();
        }
    }

    /**
     * Checks if the field has a neighbour with a ball of
     * the same colour and if the field exists.
     * @requires field != null && row >= 0 && && row < 7 && col < 7 && col >= 0
     * @param row the row of the board
     * @param col the column of the board
     * @return false if the ball in the field has a neighbour with the same colour, true otherwise
     */
    public boolean hasSameNeighbours(int row, int col) {
        for (int i = 0; i < getNeighboursIndex(row, col).length; i++) {
            if (isField(getNeighboursIndex(row, col)[i])) {
                if (ballHasSameColour(getField(row, col), getField(getNeighboursIndex(row, col)[i]))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if the field has a neighbour with a ball of
     * the same colour and if the field exists.
     * @requires field != null && index >= 0 && < 49
     * @param index the index of the board
     * @return false if the ball in the field has a neighbour with the same colour, true otherwise
     */
    public boolean hasSameNeighbours(int index) {
        for (int i = 0; i < getNeighboursIndex(index).length; i++) {
            if (isField(getNeighboursIndex(index)[i])) {
                if (ballHasSameColour(getField(index), getField(getNeighboursIndex(index)[i]))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns an array with values the indexes of the neighbours of a field
     * or -1 if there is no neighbour.
     * @requires a valid row and a valid column from the board
     * @param row the row of the field
     * @param col the column of the field.
     * @return neighbours(an array of the indexes).
     */
    public int[] getNeighboursIndex(int row, int col) {
        int[] neighbours = new int[]{-1, -1, -1, -1};

        if (isField(row, col)) {
            if (isField(row, col - 1)) {
                neighbours[0] = getField(row, col - 1).getIndex();
            }
            if (isField(row, col + 1)) {
                neighbours[1] = getField(row, col + 1).getIndex();
            }
            if (isField(row - 1, col)) {
                neighbours[2] = getField(row - 1, col).getIndex();
            }
            if (isField(row + 1, col)) {
                neighbours[3] = getField(row + 1, col).getIndex();
            }
        }
        return neighbours;
    }

    /**
     * Returns an array with values the indexes of the neighbours of a field
     * or -1 if there is no neighbour.
     * @requires a valid index from the board
     * @param index the index of the field.
     * @return neighbours(an array of the indexes).
     */
    public int[] getNeighboursIndex(int index) {
        int[] neighbours = new int[]{-1, -1, -1, -1};

        if (isField(index)) {
            if (isField(index - 1) && getRow(index) == getRow(index - 1)) {
                neighbours[0] = getField(index - 1).getIndex();
            }
            if (isField(index + 1) && getRow(index) == getRow(index + 1)) {
                neighbours[1] = getField(index + 1).getIndex();
            }
            if (isField(index - 7) && getCol(index) == getCol(index - 7)) {
                neighbours[2] = getField(index - 7).getIndex();
            }
            if (isField(index + 7) && getCol(index) == getCol(index + 7)) {
                neighbours[3] = getField(index + 7).getIndex();
            }
        }

        return neighbours;
    }

    /**
     * Fills the map with all the ball colours as
     * keys and all the values as the initial number
     * of the balls from the colour.
     * @ensures colourCounter is initialised
     */
    private void fillMap() {
        colourCounter.put(Colour.BLUE, blueCounter);
        colourCounter.put(Colour.RED, redCounter);
        colourCounter.put(Colour.YELLOW, yellowCounter);
        colourCounter.put(Colour.GREEN, greenCounter);
        colourCounter.put(Colour.PURPLE, purpleCounter);
        colourCounter.put(Colour.ORANGE, orangeCounter);
    }

    /**
     * Sets all the values in the colourCounter Map
     * to 8 for every colour.
     */
    public void setColourCounterValuesToMax() {
        blueCounter = 8;
        redCounter = 8;
        greenCounter = 8;
        yellowCounter = 8;
        orangeCounter = 8;
        purpleCounter = 8;

    }

    /**
     * Sets all the values in the colourCounter Map
     * to 0 for every colour.
     */
    public void setColourCounterValuesToZero() {
        blueCounter = 0;
        redCounter = 0;
        greenCounter = 0;
        yellowCounter = 0;
        orangeCounter = 0;
        purpleCounter = 0;
    }

    /**
     * Checks if the board is filled with 8 colours of every
     * type of ball.
     * @requires boardFields[i][j] != null
     * @param boardFields the double array of fields from the board.
     * @return true if the board is ready to be used, false otherwise
     */
    public boolean boardIsReady(Field[][] boardFields) {
        int blueCount = 0;
        int redCount = 0;
        int purpleCount = 0;
        int yellowCount = 0;
        int orangeCount = 0;
        int greenCount = 0;

        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                switch (boardFields[i][j].getBall().getColour()) {
                    case BLUE:
                        blueCount++;
                        break;

                    case RED:
                        redCount++;
                        break;

                    case GREEN:
                        greenCount++;
                        break;

                    case PURPLE:
                        purpleCount++;
                        break;

                    case ORANGE:
                        orangeCount++;
                        break;

                    case YELLOW:
                        yellowCount++;
                        break;

                    default:
                        break;
                }
            }
        }
        return blueCount == 8 && redCount == 8 && greenCount == 8 && purpleCount == 8 && orangeCount == 8 && yellowCount == 8;
    }

    /**
     * Increases the count of the balls depending on their colour.
     * @param field the field of the board
     * @requires field != null
     */
    public void increaseCount(Field field) {
        switch (field.getBall().getColour()) {
            case BLUE:
                if (blueCounter < 8) {
                    blueCounter++;
                }
                break;

            case RED:
                if (redCounter < 8) {
                    redCounter++;
                }
                break;

            case GREEN:
                if (greenCounter < 8) {
                    greenCounter++;
                }
                break;

            case PURPLE:
                if (purpleCounter < 8) {
                    purpleCounter++;
                }
                break;

            case ORANGE:
                if (orangeCounter < 8) {
                    orangeCounter++;
                }
                break;

            case YELLOW:
                if (yellowCounter < 8) {
                    yellowCounter++;
                }
                break;

            default:
                break;
        }
    }

    /**
     * Reduces the number of the balls depending on their colour.
     * @param field the field of the board
     * @requires field != null
     */
    public void subtractCount(Field field) {
        switch (field.getBall().getColour()) {
            case BLUE:
                blueCounter--;
                break;

            case RED:
                redCounter--;
                break;

            case GREEN:
                greenCounter--;
                break;

            case PURPLE:
                purpleCounter--;
                break;

            case ORANGE:
                orangeCounter--;
                break;

            case YELLOW:
                yellowCounter--;
                break;

            default:
                break;
        }
    }

    /**
     * Returns the number of the balls from a specific colour
     * that is not on the board.
     * @param field the field of the board
     * @return the number of balls remaining outside the board.
     */
    public int getColourCounter(Field field) {
        switch (field.getBall().getColour()) {
            case BLUE:
                return blueCounter;

            case RED:
                return redCounter;

            case GREEN:
                return greenCounter;

            case YELLOW:
                return yellowCounter;

            case ORANGE:
                return orangeCounter;

            case PURPLE:
                return purpleCounter;

            default:
                return -1;
        }
    }

    /**
     * Checks if two balls have the same colour.
     * @requires field1 != null && field2 != null
     * @param field1 first ball.
     * @param field2 second ball.
     * @return true if the balls have the same colour, false otherwise.
     */
    public boolean ballHasSameColour(Field field1, Field field2) {
        return field1.getBall().getColour().equals(field2.getBall().getColour());
    }

    /**
     * Sets all the fields from the board to Colour.EMPTY.
     * @ensures fields[i][j] != null
     */
    public void setBoardFieldsToEmpty() {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                fields[i][j] = new Field(new Ball(Colour.EMPTY), j + i * DIM);
            }
        }
    }

    /**
     * Creates a deep copy of the board completely identical to the original.
     * @return newBoard which is a copy of the game board.
     */
    public Board deepCopy() {
        Board newBoard = new Board();
        for (int i = 0; i < DIM; i++) {
            System.arraycopy(this.fields[i], 0, newBoard.fields[i], 0, DIM);
        }
        return newBoard;
    }

    /**
     * Checks if the entered row is valid.
     * @ensures the row exists
     * @param row the row of the board.
     * @return true if the row is between 0 and 6.
     */
    public boolean isValidRow(int row) {
        return row >= 0 && row < DIM;
    }

    /**
     * Checks if the entered column is valid.
     * @ensures the column exists
     * @param col the column of the board.
     * @return true if the column is between 0 and 6.
     */
    public boolean isValidCol(int col) {
        return col >= 0 && col < DIM;
    }

    /**
     * Checks if the field is valid through the index.
     * @requires i >= 0 && i <49
     * @ensures the field exists
     * @param index index of the field of the board.
     * @return true if index >= 0 && index < DIM * DIM.
     */
    public boolean isField(int index) {
        return index >= 0 && index < DIM * DIM;
    }

    /**
     * Checks if the field is valid through the row and the column.
     * @requires row >= 0 && row <7 && col >= 0 && col <7
     * @ensures the field exists
     * @param row the row of the board.
     * @param col the column of the board.
     * @return true if row >= 0 && row <= DIM && col >= 0 && col <= DIM.
     */
    public boolean isField(int row, int col) {
        return isValidRow(row) && isValidCol(col);
    }

    /**
     * Returns the value of the row at which the index is.
     * @requires index >= 0 && index < 49
     * @param index the index of the field
     * @return the index of the row or -1 if there is no such row
     */
    public int getRow(int index) {
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                if (fields[i][j] == getField(index)) {
                    return i;
                }
            }
        }
        return -1;
    }


    /**
     * Returns the value of the column at which the index is.
     * @requires index >= 0 && index < 49
     * @param index the index of the field
     * @return the index of the column or -1 if there is no such column
     */
    public int getCol(int index) {
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                if (fields[i][j] == getField(index)) {
                    return j;
                }
            }
        }
        return -1;
    }

    /**
     * Returns the value of the field with index i of the board.
     * @ensures a valid field is returned
     * @param i the index of the field.
     * @return fields with index value i or null if there is no such field.
     */
    public Field getField(int i) {
        if (isField(i)) {
            for (int j = 0; j < DIM; j++) {
                for (int k = 0; k < DIM; k++) {
                    if (fields[j][k].getIndex() == i) {
                        return fields[j][k];
                    }
                }
            }
        }
        return null;
    }

    /**
     * Returns the value of the field with row = row and column = col or null otherwise.
     * @ensures a valid field is returned
     * @param row the row of the board.
     * @param col the column of the board.
     * @return the field with row == row && column == col or null if there is no such field.
     */
    public Field getField(int row, int col) {
        if (isField(row, col)) {
            return fields[row][col];
        }
        return null;
    }

    /**
     * Checks if the field has a ball in it by looking at the field index.
     * @requires i >= 0 && i <49
     * @param i the index of the field.
     * @return true if ball == null || false if ball != null.
     */
    public boolean isEmptyField(int i) {
        return getField(i).getBall().getColour() == Colour.EMPTY;
    }

    /**
     * Checks if the field has a ball in it by looking at the row and column.
     * @requires row >= 0 && row <7 && col >= 0 && col <7
     * @param row the row of the board.
     * @param col the column of the board.
     * @return true if ball == null, false if ball != null.
     */
    public boolean isEmptyField(int row, int col) {
        return getField(row, col).getBall().getColour() == Colour.EMPTY;
    }

    /**
     * Sets or removes the ball on the field if the field has a valid index.
     * @requires ball != null && i >= 0 && i <49
     * @param colour the colour to be placed or removed.
     * @param i the index of the field.
     */
    public void setField(Colour colour, int i) {
        if (isField(i)) {
            getField(i).getBall().setColour(colour);
        }
    }

    /**
     * Sets or removes the ball on the specified field if the field exists.
     * @requires ball != null && row >= 0 && row <7 && col >= 0 && col <7
     * @param colour the colour to be placed or removed.
     * @param row the row of the field.
     * @param col the column of the field.
     */
    public void setField(Colour colour, int row, int col) {
        if (isField(row, col)) {
            getField(row, col).getBall().setColour(colour);
        }
    }

    public String getField(){
        StringBuilder fieldStr = new StringBuilder();
        if(this.fields != null){
            for(Field[] i : this.fields){
                for(Field f : i){
                	//System.out.println("ball index=" + f.getIndex() + " colour=" + f.getBall().getColourInt());
                    fieldStr.append(f.getBall().getColourInt()).append("~");
                }
            }
        }
        return fieldStr.toString();
    }

    /**
     * Prints the board.
     * @return a String representing the Collecto board.
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder(TOP_COLUMNS + "\n" + LINE + "\n" + "  07 | ");
        int count = 0;

        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                s.append(getField(i, j).toString()).append(" | ");
                count++;
            }
            s.append(String.format("%02d", i)).append("\n").append(LINE);
            if (count < 48) {
                s.append("\n" + "  ").append(String.format("%02d", i + 8)).append(" | ");
            }
        }
        s.append("\n" + BOTTOM_COLUMNS);

        return s.toString();
    }

    // -- Getters for the different colours -------------------------------------

    public int getBlueCounter() {
        return blueCounter;
    }

    public int getRedCounter() {
        return redCounter;
    }

    public int getOrangeCounter() {
        return orangeCounter;
    }

    public int getGreenCounter() {
        return greenCounter;
    }

    public int getYellowCounter() {
        return yellowCounter;
    }

    public int getPurpleCounter() {
        return purpleCounter;
    }

    public Map<Colour, Integer> getColourCounter() {
        return colourCounter;
    }

    /**
     * Checks if the game is over once there are no possible moves of either type.
     * @param move an instance of the Move class
     * @return true if there are no possible moves left
     */
    public boolean gameOver(Move move) {
        return move.noPossibleDoubleMoves() && move.noPossibleSingleMoves();
    }

}
