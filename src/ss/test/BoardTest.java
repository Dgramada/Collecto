package ss.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ss.gamedesign.Ball;
import ss.gamedesign.Board;
import ss.gamedesign.Colour;
import ss.gamedesign.Field;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
    private Board board;
    private static final int DIM = 7;

    @BeforeEach
    public void setUp() {
        board = new Board();
    }


    @Test
    public void testHasSameNeighbours1() {
        board.setField(Colour.RED, 0, 0);
        board.setField(Colour.RED, 0, 1);
        assertTrue(board.hasSameNeighbours(0, 0));
    }

    @Test
    public void testHasSameNeighbours2() {
        board.setField(Colour.RED, 26);
        board.setField(Colour.RED, 27);
        assertTrue(board.hasSameNeighbours(26));
    }

    @Test
    public void testGetNeighboursIndex1() {
        int[] neighbours1 = new int[]{-1, 1, -1, 7};
        int[] neighbours2 = new int[]{25, 27, 19, 33};
        assertArrayEquals(neighbours1, board.getNeighboursIndex(0, 0));
        assertArrayEquals(neighbours2, board.getNeighboursIndex(3, 5));
    }

    @Test
    public void testGetNeighboursIndex2() {
        int[] neighbours1 = new int[]{-1, 1, -1, 7};
        int[] neighbours2 = new int[]{25, 27, 19, 33};
        assertArrayEquals(neighbours1, board.getNeighboursIndex(0));
        assertArrayEquals(neighbours2, board.getNeighboursIndex(26));
    }


    @Test
  //java.lang.NullPointerException
    public void testBoardIsReady() {
        Field[][] boardFields = new Field[DIM][DIM];

        boardFields[0][0] = new Field(new Ball(Colour.RED), 0);
        boardFields[0][1] = new Field(new Ball(Colour.YELLOW), 1);
        boardFields[0][2] = new Field(new Ball(Colour.PURPLE), 2);
        boardFields[0][3] = new Field(new Ball(Colour.ORANGE), 3);
        boardFields[0][4] = new Field(new Ball(Colour.YELLOW), 4);
        boardFields[0][5] = new Field(new Ball(Colour.RED), 5);
        boardFields[0][6] = new Field(new Ball(Colour.YELLOW), 6);

        boardFields[1][0] = new Field(new Ball(Colour.GREEN), 7);
        boardFields[1][1] = new Field(new Ball(Colour.PURPLE), 8);
        boardFields[1][2] = new Field(new Ball(Colour.YELLOW), 9);
        boardFields[1][3] = new Field(new Ball(Colour.GREEN), 10);
        boardFields[1][4] = new Field(new Ball(Colour.RED), 11);
        boardFields[1][5] = new Field(new Ball(Colour.GREEN), 12);
        boardFields[1][6] = new Field(new Ball(Colour.RED), 13);

        boardFields[2][0] = new Field(new Ball(Colour.BLUE), 14);
        boardFields[2][1] = new Field(new Ball(Colour.RED), 15);
        boardFields[2][2] = new Field(new Ball(Colour.ORANGE), 16);
        boardFields[2][3] = new Field(new Ball(Colour.BLUE), 17);
        boardFields[2][4] = new Field(new Ball(Colour.PURPLE), 18);
        boardFields[2][5] = new Field(new Ball(Colour.BLUE), 19);
        boardFields[2][6] = new Field(new Ball(Colour.ORANGE), 20);

        boardFields[3][0] = new Field(new Ball(Colour.GREEN), 21);
        boardFields[3][1] = new Field(new Ball(Colour.ORANGE), 22);
        boardFields[3][2] = new Field(new Ball(Colour.BLUE), 23);
        boardFields[3][3] = new Field(new Ball(Colour.EMPTY), 25);
        boardFields[3][4] = new Field(new Ball(Colour.RED), 25);
        boardFields[3][5] = new Field(new Ball(Colour.YELLOW), 26);
        boardFields[3][6] = new Field(new Ball(Colour.GREEN), 27);

        boardFields[4][0] = new Field(new Ball(Colour.PURPLE), 28);
        boardFields[4][1] = new Field(new Ball(Colour.BLUE), 29);
        boardFields[4][2] = new Field(new Ball(Colour.YELLOW), 30);
        boardFields[4][3] = new Field(new Ball(Colour.GREEN), 31);
        boardFields[4][4] = new Field(new Ball(Colour.BLUE), 32);
        boardFields[4][5] = new Field(new Ball(Colour.GREEN), 33);
        boardFields[4][6] = new Field(new Ball(Colour.RED), 34);

        boardFields[5][0] = new Field(new Ball(Colour.RED), 35);
        boardFields[5][1] = new Field(new Ball(Colour.GREEN), 36);
        boardFields[5][2] = new Field(new Ball(Colour.ORANGE), 37);
        boardFields[5][3] = new Field(new Ball(Colour.PURPLE), 38);
        boardFields[5][4] = new Field(new Ball(Colour.ORANGE), 39);
        boardFields[5][5] = new Field(new Ball(Colour.YELLOW), 40);
        boardFields[5][6] = new Field(new Ball(Colour.ORANGE), 41);

        boardFields[6][0] = new Field(new Ball(Colour.YELLOW), 42);
        boardFields[6][1] = new Field(new Ball(Colour.PURPLE), 43);
        boardFields[6][2] = new Field(new Ball(Colour.BLUE), 44);
        boardFields[6][3] = new Field(new Ball(Colour.ORANGE), 45);
        boardFields[6][4] = new Field(new Ball(Colour.PURPLE), 46);
        boardFields[6][5] = new Field(new Ball(Colour.BLUE), 47);
        boardFields[6][6] = new Field(new Ball(Colour.PURPLE), 48);

        assertTrue(board.boardIsReady(boardFields));
    }

    @Test
    public void testIncreaseCount() {
        Ball ball = new Ball(Colour.RED);
        Field field = new Field(ball, 26);
        board.setColourCounterValuesToZero();
        board.increaseCount(field);
        assertEquals(1, board.getRedCounter());
    }

    @Test
    public void testSubtractCount() {
        Ball ball = new Ball(Colour.RED);
        Field field = new Field(ball, 26);
        board.setColourCounterValuesToMax();
        board.subtractCount(field);
        assertEquals(7, board.getRedCounter());
    }

    @Test
    public void testBallHasSameColour() {
        Ball ball1 = new Ball(Colour.RED);
        Ball ball2 = new Ball(Colour.RED);
        Field field1 = new Field(ball1, 26);
        Field field2 = new Field(ball2, 27);
        assertTrue(board.ballHasSameColour(field1, field2));
    }

    @Test
    public void testSetBoardFieldsToEmpty() {
        board.setBoardFieldsToEmpty();
        assertEquals(0, board.getBlueCounter());
        assertEquals(0, board.getOrangeCounter());
        assertEquals(0, board.getPurpleCounter());
        assertEquals(0, board.getRedCounter());
        assertEquals(0, board.getYellowCounter());
        assertEquals(0, board.getGreenCounter());
    }

    @Test
    public void testDeepCopy() {
        Board newBoard = board.deepCopy();
        for (int i = 0; i < DIM * DIM; i++) {
            assertEquals(newBoard.getField(i), board.getField(i));
        }
    }

    @Test
    public void testIsValidRow() {
        assertTrue(board.isValidRow(DIM - 1));
        assertFalse(board.isValidRow(DIM));
    }

    @Test
    public void testIsValidCol() {
        assertTrue(board.isValidCol(DIM - 1));
        assertFalse(board.isValidCol(DIM));
    }

    @Test
    public void testIsField() {
        assertTrue(board.isField(1));
        assertFalse(board.isField(DIM * DIM));
        assertTrue(board.isField(6, 6));
        assertFalse(board.isField(DIM, DIM));
    }

    @Test
    public void testGetRow() {
        assertEquals(3, board.getRow(26));
        assertEquals(-1, board.getRow(99));
    }

    @Test
    public void testGetCol() {
        assertEquals(5, board.getCol(26));
        assertEquals(-1, board.getCol(99));
    }

    @Test
    public void testGetField() {
        assertEquals(board.getField(3, 5), board.getField(26));
    }

    @Test
    public void testIsEmptyField() {
        board.setField(Colour.EMPTY, 26);
        board.setField(Colour.RED, 27);
        assertTrue(board.isEmptyField(26));
        assertTrue(board.isEmptyField(3, 5));
        assertFalse(board.isEmptyField(27));
        assertFalse(board.isEmptyField(3, 6));
    }

    @Test
    public void testSetField() {
        board.setField(Colour.RED, 26);
        board.setField(Colour.RED, 3, 6);
        assertEquals(Colour.RED, board.getField(26).getBall().getColour());
        assertEquals(Colour.RED, board.getField(3, 6).getBall().getColour());
    }
}
