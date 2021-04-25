package ss.gamedesign;

public class Ball {

    private Colour colour;

    /**
     * Creates a ball with a colour.
     * @param colour the colour of the ball.
     */
    public Ball(Colour colour) {
        this.colour = colour;
    }

    /**
     * Returns the colour of the ball.
     * @return ball colour.
     */
    public Colour getColour() {
        return this.colour;
    }
    public int getColourInt() {
    	switch (this.colour) {
        case BLUE:
            return 1;
        case YELLOW:
            return 2;
        case RED:
            return 3;
        case ORANGE:
            return 4;
        case PURPLE:
            return 5;
        case GREEN:
            return 6;
        default:
            return 0;
    	}
    }

    /**
     * Sets the colour of the ball.
     * @param colour the ball colour.
     */
    public void setColour(Colour colour) {
        this.colour = colour;
    }

    /**
     * Sets a random colour to the colour instance variable.
     * The enum value Colour.EMPTY is unreachable.
     */
    public void setRandomColour() {
        this.colour = getRandomColour();
    }

    /**
     * @return random colour for the ball
     */
    public Colour getRandomColour() {
        switch (getRandomNumber()) {
            case 1:
                return Colour.BLUE;

            case 2:
                return Colour.YELLOW;

            case 3:
                return Colour.RED;

            case 4:
                return Colour.ORANGE;

            case 5:
                return Colour.PURPLE;

            case 6:
                return Colour.GREEN;

            default:
                return Colour.EMPTY;
        }
    }

    /**
     * Generate and return a random number between 1 and 6.
     * @return a random number between 1 and 6.
     */
    private int getRandomNumber() {
        return (int) (Math.random() * 6 + 1);
    }

}
