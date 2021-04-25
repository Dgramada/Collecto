package ss.gamedesign;

public class ASCIIArtUtils {

    /**
     * The index colors of the fields.
     */
    public static final String BLACK_BOLD_BRIGHT = "\033[1;90m"; // BLACK
    public static final String WHITE_BOLD_BRIGHT = "\033[1;97m"; // WHITE

    /**
     * The background colors of the fields.
     */
    public static final String RED_BACKGROUND_BRIGHT = "\033[0;101m"; // RED
    public static final String PURPLE_BACKGROUND_BRIGHT = "\033[0;105m"; // PURPLE
    public static final String BLUE_BACKGROUND_BRIGHT = "\033[0;104m"; // BLUE
    public static final String GREEN_BACKGROUND_BRIGHT = "\033[0;102m"; // GREEN
    public static final String YELLOW_BACKGROUND_BRIGHT = "\033[0;103m"; // YELLOW
    public static final String ORANGE_BACKGROUND_BRIGHT = "\u001b[48;5;202m"; // ORANGE

    /**
     * Reset for the console colours.
     */
    public static final String RESET = "\033[0m";  // Text Reset

    /**
     * Depending on what color the ball is in the field,
     * the method returns the index of the field and sets the
     * background color to the color of the ball in that field.
     * @requires field != null
     * @ensures field is colored in console
     * @param field the field of the board
     * @return a String with a colored background and index
     */
    public static String getASCIIBallLetter(Field field) {

        switch (field.getBall().getColour()) {
            case BLUE:
                return BLUE_BACKGROUND_BRIGHT + WHITE_BOLD_BRIGHT + String.format("%02d", field.getIndex()) + RESET;

            case RED:
                return RED_BACKGROUND_BRIGHT + WHITE_BOLD_BRIGHT + String.format("%02d", field.getIndex()) + RESET;

            case PURPLE:
                return PURPLE_BACKGROUND_BRIGHT + WHITE_BOLD_BRIGHT + String.format("%02d", field.getIndex()) + RESET;

            case GREEN:
                return GREEN_BACKGROUND_BRIGHT + WHITE_BOLD_BRIGHT + String.format("%02d", field.getIndex()) + RESET;

            case ORANGE:
                return ORANGE_BACKGROUND_BRIGHT + WHITE_BOLD_BRIGHT + String.format("%02d", field.getIndex()) + RESET;

            case YELLOW:
                return YELLOW_BACKGROUND_BRIGHT + BLACK_BOLD_BRIGHT + String.format("%02d", field.getIndex()) + RESET;

            default:
                return String.format("%02d", field.getIndex());
        }
    }
}
