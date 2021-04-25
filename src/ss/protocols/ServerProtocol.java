package ss.protocols;

import ss.gamedesign.NetworkGame;
import ss.server.CollectoClientHandler;

import java.io.IOException;

/**
 * Defines the methods that the Collecto Server should support. The results
 * should be returned to the client.
 * @author Yordan Tsintsov and Yunchen Sun
 */
public interface ServerProtocol {

    void doMove(String gameName, String[] move, CollectoClientHandler handler) throws IOException;

    void doQuit(CollectoClientHandler handler, NetworkGame game);

    void doListClients(CollectoClientHandler handler);

    void doLogin(String username, CollectoClientHandler handler);

    void doGameOver(CollectoClientHandler handler);

    void doNewGame(String userName, String gameName, int capacity, CollectoClientHandler handler);

    void doDisconnect(CollectoClientHandler collectoClientHandler);

    void doQUEUE(String gameName, CollectoClientHandler handler);

    public String doHello();
}
