package ss.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ss.gamedesign.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerTest {

    private Player[] players;

    private Board board;

    private Move move;

    @BeforeEach
    void setUp() {
        board = new Board();
        move = new Move(board);
        players = new Player[2];
        players[0] = new HumanPlayer("Yordan");
        players[1] = new HumanPlayer("Yunchen");
    }

    /**
     * Test if map with the players balls is updated correctly
     * for both players.
     */
    @Test
    void increasePlayerBallTest() {
        players[0].increasePlayerBalls(Colour.ORANGE);
        players[0].fillPlayerBalls();
        assertEquals(1, players[0].getPlayerBalls().get(Colour.ORANGE));

        players[1].increasePlayerBalls(Colour.BLUE);
        players[1].increasePlayerBalls(Colour.BLUE);
        players[1].fillPlayerBalls();
        assertEquals(2, players[1].getPlayerBalls().get(Colour.BLUE));
        assertEquals(0, players[1].getPlayerBalls().get(Colour.RED));
    }

    /**
     * Test if the players points are correctly calculated.
     */
    @Test
    void setPointsTest() {
        players[1].increasePlayerBalls(Colour.BLUE);
        players[1].increasePlayerBalls(Colour.BLUE);
        players[1].increasePlayerBalls(Colour.BLUE);
        players[1].increasePlayerBalls(Colour.RED);
        players[1].increasePlayerBalls(Colour.RED);
        players[1].increasePlayerBalls(Colour.YELLOW);
        players[1].increasePlayerBalls(Colour.YELLOW);
        players[1].increasePlayerBalls(Colour.YELLOW);
        players[1].fillPlayerBalls();
        players[1].setPoints();
        assertEquals(2, players[1].getPoints());

        players[0].setPoints();
        assertEquals(0, players[0].getPoints());
    }

    /**
     * Test if the number of balls a player has is correctly calculated.
     */
    @Test
    void setNumberOfBallsTest() {
        players[1].increasePlayerBalls(Colour.ORANGE);
        players[1].increasePlayerBalls(Colour.BLUE);
        players[1].increasePlayerBalls(Colour.GREEN);
        players[1].increasePlayerBalls(Colour.RED);
        players[1].increasePlayerBalls(Colour.RED);
        players[1].increasePlayerBalls(Colour.YELLOW);
        players[1].increasePlayerBalls(Colour.PURPLE);
        players[1].increasePlayerBalls(Colour.YELLOW);
        players[1].fillPlayerBalls();
        players[1].setNumberOfBalls();
        assertEquals(8, players[1].getNumberOfBalls());

        players[0].increasePlayerBalls(Colour.GREEN);
        players[0].increasePlayerBalls(Colour.RED);
        players[0].increasePlayerBalls(Colour.RED);
        players[0].increasePlayerBalls(Colour.YELLOW);
        players[0].fillPlayerBalls();
        players[0].setNumberOfBalls();
        assertEquals(4, players[0].getNumberOfBalls());
    }

}
