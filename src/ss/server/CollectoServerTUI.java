package ss.server;

import ss.utils.TextIO;

import java.io.PrintWriter;

/**
 * Hotel Server TUI for user input and user messages.
 * @author Yordan Tsintsov and Yunchen Sun
 */
public class CollectoServerTUI implements CollectoServerView {

    /** The PrintWriter to write messages to. */
    private PrintWriter console;

    /**
     * Constructs a new CollectoServerTUI. Initializes the console.
     */
    public CollectoServerTUI() {
        console = new PrintWriter(System.out, true);
    }

    @Override
    public void showMessage(String message) {
        console.println(message);
    }

    @Override
    public int getInt(String question) {
        showMessage(question);
        return TextIO.getInt();
    }

    @Override
    public String getString(String question) {
        showMessage(question);
        return TextIO.getlnString();
    }

    @Override
    public boolean getBoolean(String question) {
        showMessage(question);
        return TextIO.getBoolean();
    }
}
