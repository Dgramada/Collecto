package ss.protocols;

/**
 * Client Protocol messages for Collecto Game Application.
 */
public class ProtocolMessages {

    /**
     * Delimiter used to separate arguments sent over the network.
     */
    public static final String DELIMITER = "~";

    /**
     * Marks the end of a message.
     */
    public static final String ENDLINE = "\n";

    /**
     * List of all the server-side commands.
     */
    public static final String HELLO = "HELLO";
    public static final String LOGIN = "LOGIN";
    public static final String ALREADYLOGGEDIN = "ALREADYLOGGEDIN";
    public static final String LIST = "LIST";
    public static final String QUEUE = "QUEUE";
    public static final String NEWGAME = "NEWGAME";
    public static final String MOVE = "MOVE";
    public static final String GAMEOVER = "GAMEOVER";

    /**
     * Error message.
     */
    public static final String ERROR = "ERROR";
}
