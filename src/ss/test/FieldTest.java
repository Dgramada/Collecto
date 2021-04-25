package ss.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ss.gamedesign.Ball;
import ss.gamedesign.Colour;
import ss.gamedesign.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FieldTest {

    private Field field;
    private Ball ball;

    @BeforeEach
    public void setUp() {
        ball = new Ball(Colour.RED);
        field = new Field(ball, 0);
    }

    @Test
    public void testGetBall() {
        assertEquals(ball, field.getBall());
    }

    @Test
    public void testSetBall() {
        Ball ball2 = new Ball(Colour.PURPLE);
        field.setBall(ball2);
        assertEquals(ball2, field.getBall());
    }

    @Test
    public void testGetIndex() {
        assertEquals(0, field.getIndex());
    }

    @Test
    public void testSetIndex() {
        int index2 = 1;
        field.setIndex(index2);
        assertEquals(index2, field.getIndex());
    }

}
