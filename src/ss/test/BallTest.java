package ss.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ss.gamedesign.Ball;
import ss.gamedesign.Colour;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BallTest {

    private Ball ball;

    @BeforeEach
    public void setUp() {
        ball = new Ball(Colour.BLUE);
    }

    @Test
    public void testGetColour() {
        assertEquals(Colour.BLUE, ball.getColour());
    }

    @Test
    public void testSetColour() {
        ball.setColour(Colour.RED);
        assertEquals(Colour.RED, ball.getColour());
    }

}
