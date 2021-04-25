package ss.gamedesign;

public class Field {

    private Ball ball;
    private int index;

    /**
     * Creates a field for the board.
     * @requires ball != null, index == int
     * @param ball the ball in the field.
     * @param index the index of the field.
     */
    public Field(Ball ball, int index) {
        this.ball = ball;
        this.index = index;
    }

    /**
     * @return the ball on the field.
     */
    public Ball getBall() {
        return this.ball;
    }

    /**
     * Sets the ball on the field.
     * @param ball the ball to be used in the field.
     */
    public void setBall(Ball ball) {
        this.ball = ball;
    }

    /**
     * @return the index of the field.
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * Sets the field index.
     * @param index the index of the field.
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Converts the field to the format used in ASCIIArtUtils.
     * @return a formatted String
     */
    @Override
    public String toString() {
        return ASCIIArtUtils.getASCIIBallLetter(this);
    }
}
