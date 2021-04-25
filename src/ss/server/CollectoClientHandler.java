package ss.server;

import ss.gamedesign.NetworkGame;
import ss.protocols.ProtocolMessages;
import ss.server.CollectoServer;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

/**
 * CollectoClientHandler for the Collecto Server application.
 * This class can handle the communication with one
 * client.
 * @author Yordan Tsintsov and Yuchen Sun
 */
public class CollectoClientHandler implements Runnable {

    /** The socket and In- and OutputStreams. */
    private BufferedReader in;
    private BufferedWriter out;
    private Socket sock;

    /** The connected CollectoServer. */
    private CollectoServer srv;

    /** Name of this ClientHandler. */
    private String name;

    /** Name of the connected user (if connected with an username). */
    private String username;

    /** True if joined to a game. */
    private boolean bjoinedtoagame;

    private NetworkGame activeGame;

    public NetworkGame getActiveGame() {
		return activeGame;
	}

	public void setActiveGame(NetworkGame activeGame) {
		this.activeGame = activeGame;
	}

	/**
     * Constructs a new CollectoClientHandler. Opens the Input and Output Streams.
     *
     * @param sock The client socket
     * @param srv  The connected server
     * @param name The name of this ClientHandler
     */
    public CollectoClientHandler(Socket sock, CollectoServer srv, String name) {
        try {
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
            this.sock = sock;
            this.srv = srv;
            this.name = name;
            this.bjoinedtoagame = false;
            this.activeGame = null;

        } catch (IOException e) {
            shutdown();
        }
    }

    /**
     * Continuously listens to client input and forwards the input to the
     * {@link #handleCommand(String)} method.
     */
    @Override
    public void run() {
        String msg;
        try {
            msg = in.readLine();
            while (msg != null) {
                System.out.println("> [" + name + "] Incoming: " + msg);
                handleCommand(msg);
                out.newLine();
                out.flush();

                msg = in.readLine();

            }
            shutdown();
        } catch (IOException e) {
            shutdown();
        }
    }

    /**
     * Sends a message to the client.
     * @param message The message to be sent
     * @throws IOException thrown when there is an IO error
     */
    public void sendMessageToClient(String message) throws IOException {
        out.write(message);
        out.newLine();
        out.flush();
    }

    /**
     * Shut down the connection to this client by closing the socket and
     * the Input and Output Streams.
     */
    private void shutdown() {
        System.out.println("> [" + name + "] Shutting down.");
        if (isJoinedToGame()) {
            srv.doQuit(this, this.activeGame);
        }
        try {
            in.close();
            out.close();
            sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        srv.removeClient(this);
    }

    public void setUser(String user) {
        this.username = user;
    }

    public void clearUser() {
        this.username = "";
    }

    public boolean isJoinedToGame() {
        return this.bjoinedtoagame;
    }

    public void joinActiveGame(NetworkGame game) {
        this.bjoinedtoagame = true;
        this.activeGame = game;
    }

    public void quitActiveGame() {
        this.bjoinedtoagame = false;
        this.activeGame = null;
    }

    public String getUsername() {
        return username;
    }

    /**
     * Handles commands received from the client by calling the according
     * methods at the Collecto Server. For example, when the message "c:dimitar"
     * is received, the method doConnect() of CollectoServer should be called
     * and the output must be sent to the client.
     * If the received input is not valid, send an "Unknown Command"
     * message to the server.
     *
     * @param msg command from client
     * @throws IOException if an IO errors occur.
     */
    private void handleCommand(String msg) throws IOException {
        String[] array = msg.split(ProtocolMessages.DELIMITER);
        String responseMessage = "";

        if (array.length == 0) {
            sendMessageToClient("Empty command received");
        }

        String command = array[0];

        switch (command) {

            case ProtocolMessages.HELLO:
                out.write(srv.doHello());
                break;

            case ProtocolMessages.LOGIN:
                if (array.length < 2) {
                    responseMessage = ProtocolMessages.ERROR + ProtocolMessages.DELIMITER + "You should specify a username when connecting to the server - LOGIN~<username>";
                    sendMessageToClient(responseMessage);
                } else {
                    srv.doLogin(array[1], this);
                }
                break;

            case ProtocolMessages.LIST:
                srv.doListClients(this);
                break;

            case ProtocolMessages.QUEUE:
                if (activeGame != null) {
                    srv.doQUEUE(activeGame.getName(), this);
                } else {
          //srv.doNewGame(username, "test", 2, this);
          srv.doQUEUE("test", this);
                    responseMessage = ProtocolMessages.ERROR + ProtocolMessages.DELIMITER + "Please create or join a game before sending ready command!";
                }
                break;

            case ProtocolMessages.MOVE:
                boolean binvalidinput = false;

                if (array.length < 2) {
                    binvalidinput = true;
                    responseMessage = ProtocolMessages.ERROR + ProtocolMessages.DELIMITER + "Valid move should contain one or two columns/rows selected and a direction - MOVE~<m1>:<m2>";
                } else {
                    if (array.length > 4) {
                        binvalidinput = true;
                        responseMessage = ProtocolMessages.ERROR + ProtocolMessages.DELIMITER + "Valid move should contain at least one ball selected and a direction - MOVE~<m1>:<m2>>";
                    } else {
                        assert activeGame != null;
                        if (!activeGame.getMove().noPossibleSingleMoves()) {
                            if (array.length != 2) {
                                try {
                                    Integer.parseInt(array[1]);
                                } catch (NumberFormatException e) {
                                    responseMessage = ProtocolMessages.ERROR + ProtocolMessages.DELIMITER + "Invalid number in the command" + array[1];
                                    binvalidinput = true;
                                }
                            }
                        } else {
                            if (array.length != 3) {
                                try {
                                    Integer.parseInt(array[1]);
                                    Integer.parseInt(array[2]);
                                } catch (NumberFormatException e) {
                                    responseMessage = ProtocolMessages.ERROR + ProtocolMessages.DELIMITER + "Invalid number in the command" + array[1] + ProtocolMessages.DELIMITER + array[2];
                                    binvalidinput = true;
                                }
                            }
                        }
                    }
                }


                // If the input is valid, then call doMove
                if (binvalidinput) {
                    sendMessageToClient(responseMessage);
                } else {

                    String[] tempMove = msg.split(ProtocolMessages.DELIMITER);
                    String[] move = Arrays.copyOfRange(tempMove, 1, tempMove.length);

                    srv.doMove(activeGame.getName(), move, this);
                }
            break;
        }
    }

}
