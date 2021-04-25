package ss.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ss.gamedesign.*;

import static org.junit.jupiter.api.Assertions.*;

public class MoveTest {

    private Board board;
    private Board copy;
    private Move move;

    @BeforeEach
    public void setUp() {
        board = new Board();
        move = new Move(board);
        copy = board.deepCopy();
    }

    // -- ROW CONTROL TESTING

    @Test
    void rowCanBeMoved() {
        board.setBoardFieldsToEmpty();
        board.setField(Colour.BLUE, 0);
        board.setField(Colour.RED, 1);
        board.setField(Colour.YELLOW, 3);
        board.setField(Colour.RED, 5);

        assertTrue(move.rowCanBeMoved(0));
    }

    @Test
    void hasEmptyRightNeighbourTest() {
        board.setBoardFieldsToEmpty();
        board.setField(Colour.BLUE, 0);
        board.setField(Colour.RED, 1);
        board.setField(Colour.YELLOW, 2);
        board.setField(Colour.RED, 3);
        for (int i = 0; i < 7; i++) {
            board.setField(Colour.ORANGE, 6, i);
        }

        assertTrue(move.hasEmptyRightNeighbour(0));
        assertFalse(move.hasEmptyRightNeighbour(1));
        assertFalse(move.hasEmptyRightNeighbour(6));
    }

    @Test
    void moveRowOnRightTest() {
        board.setBoardFieldsToEmpty();
        board.setField(Colour.BLUE, 7);
        board.setField(Colour.RED, 8);
        board.setField(Colour.BLUE, 9);
        board.setField(Colour.BLUE, 10);
        board.setField(Colour.BLUE, 11);
        board.setField(Colour.BLUE, 12);
        board.setField(Colour.GREEN, 13);
        move.moveRowOnRight(1);
        assertEquals(Colour.BLUE, board.getField(7).getBall().getColour());
        assertEquals(Colour.RED, board.getField(8).getBall().getColour());
        assertEquals(Colour.BLUE, board.getField(9).getBall().getColour());
        assertEquals(Colour.BLUE, board.getField(10).getBall().getColour());
        assertEquals(Colour.BLUE, board.getField(11).getBall().getColour());
        assertEquals(Colour.BLUE, board.getField(12).getBall().getColour());
        assertEquals(Colour.GREEN, board.getField(13).getBall().getColour());

        board.setField(Colour.EMPTY, 11);
        move.moveRowOnRight(1);
        assertEquals(Colour.EMPTY, board.getField(7).getBall().getColour());
        assertEquals(Colour.BLUE, board.getField(8).getBall().getColour());
        assertEquals(Colour.RED, board.getField(9).getBall().getColour());
        assertEquals(Colour.BLUE, board.getField(10).getBall().getColour());
        assertEquals(Colour.BLUE, board.getField(11).getBall().getColour());
        assertEquals(Colour.BLUE, board.getField(12).getBall().getColour());
        assertEquals(Colour.GREEN, board.getField(13).getBall().getColour());
    }

    @Test
    void hasEmptyLeftNeighbourTest() {
        board.setBoardFieldsToEmpty();
        board.setField(Colour.RED, 0);
        board.setField(Colour.GREEN, 4);
        board.setField(Colour.BLUE, 7);
        board.setField(Colour.ORANGE, 8);
        board.setField(Colour.GREEN, 9);
        board.setField(Colour.BLUE, 10);
        board.setField(Colour.YELLOW, 11);
        board.setField(Colour.GREEN, 12);
        assertTrue(move.hasEmptyLeftNeighbour(0));
        assertFalse(move.hasEmptyLeftNeighbour(1));

    }

    @Test
    void moveRowOnLeftTest() {
        board.setBoardFieldsToEmpty();
        board.setField(Colour.RED, 7);
        board.setField(Colour.RED, 8);
        board.setField(Colour.BLUE, 9);
        board.setField(Colour.BLUE, 10);
        board.setField(Colour.YELLOW, 11);
        board.setField(Colour.BLUE, 12);
        board.setField(Colour.GREEN, 13);
        move.moveRowOnLeft(1);
        assertEquals(Colour.RED, board.getField(7).getBall().getColour());
        assertEquals(Colour.RED, board.getField(8).getBall().getColour());
        assertEquals(Colour.BLUE, board.getField(9).getBall().getColour());
        assertEquals(Colour.BLUE, board.getField(10).getBall().getColour());
        assertEquals(Colour.YELLOW, board.getField(11).getBall().getColour());
        assertEquals(Colour.BLUE, board.getField(12).getBall().getColour());
        assertEquals(Colour.GREEN, board.getField(13).getBall().getColour());

        board.setField(Colour.EMPTY, 7);
        move.moveRowOnLeft(1);
        assertEquals(Colour.RED, board.getField(7).getBall().getColour());
        assertEquals(Colour.BLUE, board.getField(8).getBall().getColour());
        assertEquals(Colour.BLUE, board.getField(9).getBall().getColour());
        assertEquals(Colour.YELLOW, board.getField(10).getBall().getColour());
        assertEquals(Colour.BLUE, board.getField(11).getBall().getColour());
        assertEquals(Colour.GREEN, board.getField(12).getBall().getColour());
        assertEquals(Colour.EMPTY, board.getField(13).getBall().getColour());
    }

    // -- COLUMN CONTROL TESTING

    @Test
    void columnCanBeMovedTest() {
        board.setBoardFieldsToEmpty();
        board.setField(Colour.GREEN, 0);
        board.setField(Colour.GREEN, 7);
        board.setField(Colour.GREEN, 14);
        board.setField(Colour.GREEN, 21);

        assertTrue(move.columnCanBeMoved(0));
    }

    @Test
    void hasEmptyLowerNeighbourTest() {
        board.setBoardFieldsToEmpty();
        board.setField(Colour.RED, 0);
        board.setField(Colour.GREEN, 7);
        board.setField(Colour.BLUE, 14);
        board.setField(Colour.ORANGE, 21);
        board.setField(Colour.GREEN, 28);
        board.setField(Colour.BLUE, 35);
        board.setField(Colour.YELLOW, 42);
        board.setField(Colour.GREEN, 12);

        assertFalse(move.hasEmptyLowerNeighbour(0));
        assertTrue(move.hasEmptyLowerNeighbour(5));
    }

    @Test
    void moveColumnDownTest() {
        board.setBoardFieldsToEmpty();
        board.setField(Colour.RED, 1);
        board.setField(Colour.RED, 8);
        board.setField(Colour.BLUE, 15);
        board.setField(Colour.BLUE, 22);
        board.setField(Colour.YELLOW, 29);
        board.setField(Colour.BLUE, 36);
        board.setField(Colour.GREEN, 43);
        move.moveColumnDown(1);
        assertEquals(Colour.RED, board.getField(1).getBall().getColour());
        assertEquals(Colour.RED, board.getField(8).getBall().getColour());
        assertEquals(Colour.BLUE, board.getField(15).getBall().getColour());
        assertEquals(Colour.BLUE, board.getField(22).getBall().getColour());
        assertEquals(Colour.YELLOW, board.getField(29).getBall().getColour());
        assertEquals(Colour.BLUE, board.getField(36).getBall().getColour());
        assertEquals(Colour.GREEN, board.getField(43).getBall().getColour());

        board.setField(Colour.EMPTY, 43);
        move.moveColumnDown(1);
        assertEquals(Colour.EMPTY, board.getField(1).getBall().getColour());
        assertEquals(Colour.RED, board.getField(8).getBall().getColour());
        assertEquals(Colour.RED, board.getField(15).getBall().getColour());
        assertEquals(Colour.BLUE, board.getField(22).getBall().getColour());
        assertEquals(Colour.BLUE, board.getField(29).getBall().getColour());
        assertEquals(Colour.YELLOW, board.getField(36).getBall().getColour());
        assertEquals(Colour.BLUE, board.getField(43).getBall().getColour());
    }

    @Test
    void hasEmptyUpperNeighbourTest() {
        board.setBoardFieldsToEmpty();
        board.setField(Colour.YELLOW, 0);
        board.setField(Colour.GREEN, 7);
        board.setField(Colour.BLUE, 14);
        board.setField(Colour.ORANGE, 21);
        board.setField(Colour.GREEN, 28);
        board.setField(Colour.BLUE, 35);
        board.setField(Colour.EMPTY, 42);
        board.setField(Colour.GREEN, 12);
        board.setField(Colour.GREEN, 19);

        assertFalse(move.hasEmptyUpperNeighbour(0));
        assertTrue(move.hasEmptyUpperNeighbour(5));
    }

    @Test
    void moveColumnUpTest() {
        board.setBoardFieldsToEmpty();
        board.setField(Colour.PURPLE, 1);
        board.setField(Colour.RED, 8);
        board.setField(Colour.BLUE, 15);
        board.setField(Colour.ORANGE, 22);
        board.setField(Colour.YELLOW, 29);
        board.setField(Colour.BLUE, 36);
        board.setField(Colour.RED, 43);

        move.moveColumnUp(1);
        assertEquals(Colour.PURPLE, board.getField(1).getBall().getColour());
        assertEquals(Colour.RED, board.getField(8).getBall().getColour());
        assertEquals(Colour.BLUE, board.getField(15).getBall().getColour());
        assertEquals(Colour.ORANGE, board.getField(22).getBall().getColour());
        assertEquals(Colour.YELLOW, board.getField(29).getBall().getColour());
        assertEquals(Colour.BLUE, board.getField(36).getBall().getColour());
        assertEquals(Colour.RED, board.getField(43).getBall().getColour());

        board.setField(Colour.EMPTY, 1);
        move.moveColumnUp(1);
        assertEquals(Colour.RED, board.getField(1).getBall().getColour());
        assertEquals(Colour.BLUE, board.getField(8).getBall().getColour());
        assertEquals(Colour.ORANGE, board.getField(15).getBall().getColour());
        assertEquals(Colour.YELLOW, board.getField(22).getBall().getColour());
        assertEquals(Colour.BLUE, board.getField(29).getBall().getColour());
        assertEquals(Colour.RED, board.getField(36).getBall().getColour());
        assertEquals(Colour.EMPTY, board.getField(43).getBall().getColour());
    }

    // WHOLE BOARD CONTROL TESTING

    @Test
    void fillArrayListTest() {
        board.setBoardFieldsToEmpty();
        move.fillArrayList();

        assertEquals(0, move.getSameNeighboursArrayList().size());

        board.setField(Colour.ORANGE, 0);
        board.setField(Colour.ORANGE, 1);
        board.setField(Colour.ORANGE, 7);
        move.fillArrayList(0, 0); // Test first fillArrayList method

        assertEquals(3, move.getSameNeighboursArrayList().size());
        assertEquals(1, move.getSameNeighboursArrayList().get(0));
        assertEquals(7, move.getSameNeighboursArrayList().get(1));
        assertEquals(0, move.getSameNeighboursArrayList().get(2));

        board.setField(Colour.YELLOW, 48);
        board.setField(Colour.YELLOW, 41);
        board.setField(Colour.YELLOW, 47);
        move.fillArrayList(); // Test second fillArrayList method

        assertEquals(6, move.getSameNeighboursArrayList().size());
        assertEquals(48, move.getSameNeighboursArrayList().get(3));
        assertEquals(41, move.getSameNeighboursArrayList().get(4));
        assertEquals(47, move.getSameNeighboursArrayList().get(5));


    }

    @Test
    void removeSameBallsTest() {
        board.setBoardFieldsToEmpty();
        board.setField(Colour.ORANGE, 0);
        board.setField(Colour.ORANGE, 1);
        board.setField(Colour.ORANGE, 7);
        board.setField(Colour.YELLOW, 48);
        board.setField(Colour.YELLOW, 41);
        board.setField(Colour.YELLOW, 47);
        move.fillArrayList();
        move.removeSameBalls();

        assertEquals(0, move.getSameNeighboursArrayList().size());
        assertEquals(Colour.EMPTY, board.getField(0).getBall().getColour());
        assertEquals(Colour.EMPTY, board.getField(1).getBall().getColour());
        assertEquals(Colour.EMPTY, board.getField(7).getBall().getColour());
        assertEquals(Colour.EMPTY, board.getField(48).getBall().getColour());
        assertEquals(Colour.EMPTY, board.getField(41).getBall().getColour());
        assertEquals(Colour.EMPTY, board.getField(47).getBall().getColour());

    }

    @Test
    void isMoveValidTest() {
        board.setBoardFieldsToEmpty();
        board.setField(Colour.YELLOW, 0);
        board.setField(Colour.YELLOW, 1);
        board.setField(Colour.YELLOW, 7);
        board.setField(Colour.RED, 48);
        board.setField(Colour.RED, 41);
        board.setField(Colour.RED, 47);
        move.fillArrayList();
        assertTrue(move.isMoveValid());

        move.removeSameBalls();
        assertFalse(move.isMoveValid());
    }

    @Test
    void updateCopyTest() {
        board.setBoardFieldsToEmpty();
        move.updateCopy();
        for (int i = 0; i < 49; i++) {
            assertEquals(board.getField(i).getBall().getColour(), copy.getField(i).getBall().getColour());
            assertEquals(board.getField(i).getIndex(), copy.getField(i).getIndex());
        }
    }

    @Test
    void reverseMoveTest() {
        move.updateCopy();
        board.setBoardFieldsToEmpty();
        move.reverseMove();
        for (int i = 0; i < 49; i++) {
            assertEquals(copy.getField(i).getIndex(), board.getField(i).getIndex());
        }
    }

    @Test
    void fillSingleMovesMapTest() {
        board.setBoardFieldsToEmpty();
        move.updateCopy();
        move.fillSingleMovesMap();
        for (int i = 0; i < 28; i++) {
            assertFalse(move.getSingleMoves().get(i));
        }

        board.setField(Colour.ORANGE, 0);
        board.setField(Colour.ORANGE, 7);
        board.setField(Colour.ORANGE, 6);
        board.setField(Colour.GREEN, 42);
        board.setField(Colour.GREEN, 45);
        move.updateCopy();
        move.fillSingleMovesMap();

        assertTrue(move.getSingleMoves().get(0));
        assertTrue(move.getSingleMoves().get(7));
        assertTrue(move.getSingleMoves().get(6));
        assertTrue(move.getSingleMoves().get(21));
    }

    @Test
    void noPossibleSingleMovesTest() {
        board.setBoardFieldsToEmpty();
        move.updateCopy();
        move.fillSingleMovesMap();
        assertTrue(move.noPossibleSingleMoves());

        board.setField(Colour.YELLOW, 0);
        board.setField(Colour.YELLOW, 7);
        board.setField(Colour.YELLOW, 6);
        board.setField(Colour.GREEN, 42);
        board.setField(Colour.GREEN, 45);
        move.updateCopy();
        move.fillSingleMovesMap();
        assertFalse(move.noPossibleSingleMoves());
    }

    @Test
    void noPossibleDoubleMovesTest() {
        board.setBoardFieldsToEmpty();
        move.updateCopy();
        assertTrue(move.noPossibleDoubleMoves());

        board.setField(Colour.ORANGE, 0);
        board.setField(Colour.ORANGE, 44);
        move.updateCopy();
        move.fillSingleMovesMap();
        assertFalse(move.noPossibleDoubleMoves());
    }

    @Test
    void singleMoveTest() {
        Player player = new HumanPlayer("Yordan");
        board.setBoardFieldsToEmpty();
        board.setField(Colour.ORANGE, 0);
        board.setField(Colour.ORANGE, 6);
        board.setField(Colour.ORANGE, 3);
        move.updateCopy();
        move.fillSingleMovesMap();
        move.singleMove(player, 0);


        for (int i = 0; i < 49; i++) {
            assertEquals(Colour.EMPTY, board.getField(i).getBall().getColour());
        }
    }

    @Test
    void doubleMovesTest() {
        Player player = new HumanPlayer("Yordan");
        board.setBoardFieldsToEmpty();
        board.setField(Colour.ORANGE, 0);
        board.setField(Colour.ORANGE, 48);
        System.out.println(board.toString());
        move.doubleMove(player, 20, 0);

        for (int i = 0; i < 49; i++) {
            assertEquals(Colour.EMPTY, board.getField(i).getBall().getColour());
        }
    }

}
