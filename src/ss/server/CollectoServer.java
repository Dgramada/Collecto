package ss.server;

import ss.exceptions.ExitProgram;
import ss.exceptions.InvalidMoveException;
import ss.exceptions.JoinGameException;
import ss.gamedesign.Board;
import ss.gamedesign.NetworkGame;
import ss.protocols.ProtocolMessages;
import ss.protocols.ServerProtocol;

import java.io.IOException;
import java.net.*;
import java.util.*;

/**
 * Server TUI for Networked Collecto Application.
 * Intended Functionality: interactively set up & monitor a new server
 *
 * @author Yordan Tsintsov and Yunchen Sun
 */
public class CollectoServer implements Runnable, ServerProtocol {

  /**
   * The ServerSocket of this CollectoServer.
   */
  private ServerSocket ssock;

  /**
   * List of CollectoClientHandlers, one for each connected client.
   */
  private List<CollectoClientHandler> clients;

  /**
   * Next client number, increasing for every new connection.
   */
  private int nextClientNo;

  /**
   * The view of this CollectoServer.
   */
  private CollectoServerTUI view;

  /**
   * The name of the Collecto server.
   */
  private static final String SERVERNAME = "YS_SURVER";

  /**
   * The games on the server.
   */
  private HashMap<String, NetworkGame> games;

  private HashSet<String> connectedUsers;

  /**
   * Constructs a new CollectoServer. Initializes the clients list,
   * the view and the nextClientNo.
   */
  public CollectoServer() {
    clients = new ArrayList<>();
    games = new HashMap<>();
    connectedUsers = new HashSet<>();
    view = new CollectoServerTUI();
    nextClientNo = 1;
  }

  // ------------------ Main --------------------------

  /**
   * Start a new CollectoServer.
   */
  public static void main(String[] args) {
    CollectoServer server = new CollectoServer();
    System.out.println("Welcome to the Abalone Server! Starting...");
    new Thread(server).start();
  }

  // --------------------------------------------------

  /**
   * Returns the name of the Collecto server.
   *
   * @return the name of the Collecto server.
   * @requires Collecto != null;
   */
  public String getServerName() {
    return SERVERNAME;
  }

  /**
   * Opens a new socket by calling {@link #setup()} and starts a new
   * AbaloneClientHandler for every connecting client.
   * If {@link #setup()} throws a ExitProgram exception, stop the program.
   * In case of any other errors, ask the user whether the setup should be
   * ran again to open a new socket.
   */
  @Override
  public void run() {
    boolean openNewSocket = true;
    while (openNewSocket) {
      try {
        // Sets up the Collecto application
        setup();

        while (true) {
          Socket sock = ssock.accept();
          String name = "Client " + String.format("%02d", nextClientNo++);
          view.showMessage("New client [" + name + "] connected!");
          CollectoClientHandler handler = new CollectoClientHandler(sock, this, name);
          new Thread(handler).start();
          clients.add(handler);
        }

      } catch (ExitProgram e1) {
        // If setup() throws an ExitProgram exception,
        // stop the program.
        openNewSocket = false;
      } catch (IOException e) {
        System.out.println("A server IO error occurred: " + e.getMessage());

        if (!view.getBoolean("Do you want to open a new socket?")) {
          openNewSocket = false;
        }
      }
    }
    view.showMessage("See you later!");
  }

  /**
   * Sets up a new Collecto using and opens a new
   * ServerSocket at localhost on a user-defined port.
   * The user is asked to input a port, after which a socket is attempted
   * to be opened. If the attempt succeeds, the method ends, If the
   * attempt fails, the user decides to try again, after which an
   * ExitProgram exception is thrown or a new port is entered.
   *
   * @throws ExitProgram if a connection can not be created on the given
   *                     port and the user decides to exit the program.
   * @ensures a serverSocket is opened.
   */
  public void setup() throws ExitProgram, UnknownHostException {

    ssock = null;
    while (ssock == null) {
      int port = view.getInt("Please enter the server port.");
//            String host = view.getString("Pleas enter the IP address.");
//            InetAddress ip = InetAddress.getByName(host);

      // try to open a new ServerSocket
      try {
//                try (final DatagramSocket socket = new DatagramSocket()) {
//                    socket.connect(ip, port);
////                    addr = socket.getLocalAddress().getHostAddress();
//                }
        view.showMessage("Attempting to open a socket on port " + port + "...");
        ssock = new ServerSocket(port, 0, InetAddress.getByName("127.0.0.1"));
        view.showMessage("Server started at port " + port);
      } catch (IOException e) {
        view.showMessage("ERROR: could not create a socket on port " + port + ".");

        if (!view.getBoolean("Do you want to try again?")) {
          throw new ExitProgram("User indicated to exit the "
                  + "program.");
        }
      }
    }
  }

  /**
   * Removes a clientHandler from the client list.
   *
   * @requires client != null
   */
  public void removeClient(CollectoClientHandler client) {
    this.clients.remove(client);
  }

  /**
   * Disconnect the handler connected to a client from the server(Called when a user disconnects).
   *
   * @param collectoClientHandler the handler to disconnect
   */
  @Override
  public synchronized void doDisconnect(CollectoClientHandler collectoClientHandler) {
    collectoClientHandler.clearUser();
  }

  /**
   * Processes connection request from a client app.
   */
  @Override
  public synchronized String doHello() {
    return ProtocolMessages.HELLO + ProtocolMessages.DELIMITER + "username";
  }


  /**
   * Passes a list of clients on the server to the client.
   */
  @Override
  public void doListClients(CollectoClientHandler handler) {
    StringBuilder retStr = new StringBuilder("LIST");

    if (connectedUsers.size() == 0) {
      retStr = new StringBuilder("There are no games on this server. You may join another one...");
    } else {
      for (String user : connectedUsers) {
        retStr.append(ProtocolMessages.DELIMITER).append(user);

      }
      retStr = new StringBuilder(retStr.substring(0, retStr.length() - 1));
    }
//    return retStr.toString();
    try {
      handler.sendMessageToClient(retStr.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  /**
   * Processes connection request from a client app.
   *
   * @param username the username of the user
   * @param handler  The handler that sends the request to the server
   */
  @Override
  public void doLogin(String username, CollectoClientHandler handler) {
    String retStr = "";

    if (connectedUsers.contains(username)) {
      retStr = ProtocolMessages.ERROR + ProtocolMessages.DELIMITER + username + ProtocolMessages.DELIMITER + ProtocolMessages.ALREADYLOGGEDIN;
      view.showMessage("duplicate user [" + username + "] trying to connect");
    } else {
      connectedUsers.add(username);
      handler.setUser(username);
      view.showMessage("user [" + username + "] connected!");
    }

    try {
      handler.sendMessageToClient(retStr);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Executes the move function of the board on the server for a given game(by name),
   * move and handler of the player doing the move.
   *
   * @param gameName name of the game to perform the move on
   * @param move     the move to be performed
   * @param handler  the handler for the client of the player doing the move
   */
  @Override
  public void doMove(String gameName, String[] move, CollectoClientHandler handler) throws IOException {
    String retStr = ProtocolMessages.MOVE;

    NetworkGame game = games.get(gameName);

    if (game == null) {
      retStr = ProtocolMessages.ERROR + ProtocolMessages.DELIMITER
              + "Fatal server error. Could not get the game for the move!";
      try {
        handler.sendMessageToClient(retStr);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    assert game != null;
    //view.showMessage(game.getBoard().toString());

    String[] corrMove = new String[move.length];
    try {
      //game.move(corrMove);
      game.move(move);
    } catch (InvalidMoveException e) {
      retStr = ProtocolMessages.ERROR + ProtocolMessages.DELIMITER + e.getMessage();

      try {
        handler.sendMessageToClient(retStr);
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    }


    view.showMessage(game.getBoard().toString());
    /*
    try {
      handler.sendMessageToClient(retStr);
    } catch (IOException e2) {
      e2.printStackTrace();
    }
	*/
    // propagate move to all clients

    retStr += "~" + move[move.length - 1];

    Collection<CollectoClientHandler> handlers = game.getClientHandlers();


    for (CollectoClientHandler clientHandler : handlers) {
      try {
        clientHandler.sendMessageToClient(retStr);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    if (game.isOver()) {
      if (game.isDraw()) {
        for (CollectoClientHandler clientHandler : handlers) {
          retStr = ProtocolMessages.GAMEOVER + ProtocolMessages.DELIMITER + "draw";
          try {
            clientHandler.quitActiveGame();
            clientHandler.sendMessageToClient(retStr);
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      } else {
        for (CollectoClientHandler clientHandler : handlers) {
          retStr = ProtocolMessages.GAMEOVER + ProtocolMessages.DELIMITER + "VICTORY" + ProtocolMessages.DELIMITER + handler.getUsername();
          try {
            clientHandler.sendMessageToClient(retStr);
            doQuit(clientHandler, game);
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
      return;
    }
    // Give turn to the next user
    CollectoClientHandler next = game.getNextTurnClientHandler();
    //doTurn(next.getUsername(), next);
//    String boardString = game.getBoard().getField();
//    handler.sendMessageToClient(ProtocolMessages.MOVE + "~" + boardString);

  }

  @Override
  public void doQuit(CollectoClientHandler handler, NetworkGame game) {
    if (handler.isJoinedToGame()) {
      handler.quitActiveGame();
      for (CollectoClientHandler a : game.getClientHandlers()) {
        if (a != handler) {
          try {
            a.sendMessageToClient(ProtocolMessages.GAMEOVER + ProtocolMessages.DELIMITER + "DISCONNECT");
            a.quitActiveGame();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
      games.remove(game.getName());
    }
  }

  /**
   * Called when a game is over. Sends the message to the client given through the handler.
   *
   * @param handler the handler for the user
   */
  @Override
  public void doGameOver(CollectoClientHandler handler) {
    String retStr = ProtocolMessages.GAMEOVER;

    try {
      view.showMessage("Sending game over to  user [" + handler.getUsername() + "]!");
      handler.sendMessageToClient(retStr);
      view.showMessage("Game over sent to user [" + handler.getUsername() + "]!");
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  /**
   * Creates a game given a name, password and capacity for it. Then has the player that created it join the game.
   *
   * @param userName the name of the player creating it
   * @param gameName the name for the game
   * @param capacity the capacity for the game
   * @param handler  the handler for the client calling it
   */
  @Override
  public void doNewGame(String userName, String gameName, int capacity, CollectoClientHandler handler) {
    String retStr = ProtocolMessages.NEWGAME;

    if (games.containsKey(gameName)) {
      retStr = ProtocolMessages.ERROR + ProtocolMessages.DELIMITER + " duplicate game name - " + gameName;
      view.showMessage("Duplicate game [" + gameName + "] found when trying to create new game!");
    } else {
      NetworkGame game = new NetworkGame(gameName, capacity);
      game.getBoard().initialiseBoard();
      games.put(gameName, game);
      view.showMessage("Game [" + gameName + "] created!");

      // Join to the game after the creation
      try {
        game.join(userName, handler);
        view.showMessage(userName + " joined the game  [" + gameName + "]!");

      } catch (JoinGameException e) {
        retStr = ProtocolMessages.ERROR + ProtocolMessages.DELIMITER + "Unable to jon the game after creating it " + e.getMessage();

      }
    }
    /*
    try {
      handler.sendMessageToClient(retStr);
    } catch (IOException e) {
      e.printStackTrace();
    }
    */
  }

  /**
   * Called when a user indicates they are ready to start a game.
   * Starts the game if all users are ready.
   *
   * @param gameName name of the game the user is in
   * @param handler  the handler for the user
   */
  @Override
  public synchronized void doQUEUE(String gameName, CollectoClientHandler handler) {
	String retStr = "";
    NetworkGame game = games.get(gameName);
    
    if(game == null) {
    	doNewGame(handler.getUsername(), gameName, 2, handler);
    	game = games.get(gameName);
    }else {
        try {
            game.join(handler.getUsername(), handler);
            view.showMessage(handler.getUsername() + " joined the game  [" + gameName + "]!");
          } catch (JoinGameException e) {
            retStr = ProtocolMessages.ERROR + ProtocolMessages.DELIMITER + "Unable to jon the game after creating it " + e.getMessage();
          }
    }    
    game.ready(handler.getUsername());

    retStr = ProtocolMessages.QUEUE;

    //try {
    view.showMessage("Ready message received from user [" + handler.getUsername() + "]!");
    //handler.sendMessageToClient(retStr);
    // } catch (IOException e) {
    //   e.printStackTrace();
    // }

    //Starts the game if the last user joined
    if (game.getReadyPlayers() == game.getNumberOfPlayers()) {
      for (CollectoClientHandler next : game.getClientHandlers()) {
        doStart(next);
      }

      //doTurn(game.getNextTurnClientHandler().getUsername(), game.getNextTurnClientHandler());
    }
  }

  /**
   * Sends a NEWGAME message to user to indicate the game they are in has started.
   *
   * @param handler the handler for the user
   */
  public synchronized void doStart(CollectoClientHandler handler) {
    String retStr = ProtocolMessages.NEWGAME;
    //Board board = new Board();
    NetworkGame game = handler.getActiveGame();
    
    String boardString = game.getBoard().getField();
    //String boardString = board.getField();
    try {
      view.showMessage("Sending start to  user [" + handler.getUsername() + "]!");
//      handler.sendMessageToClient(retStr + boardString + handler.getUsername());
      handler.sendMessageToClient(retStr + "~" + boardString + "bob~" + "Alice");
//      handler.sendMessageToClient(boardString);
      view.showMessage("Start sent to user [" + handler.getUsername() + "]!");
      System.out.println(retStr + "~" + boardString + "bob~" + "Alice");
      System.out.println(game.getBoard().toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Gives the turn to a user.
   *
   * @param userName name of the user to give the turn to
   * @param handler  the handler for the user
   */
  public synchronized void doTurn(String userName, CollectoClientHandler handler) {
    String retStr = ProtocolMessages.MOVE + ProtocolMessages.DELIMITER;
    retStr += userName;

    try {
      view.showMessage("Giving turn to  user [" + userName + "]!");
      handler.sendMessageToClient(retStr);
      view.showMessage("Turn given to user [" + userName + "]!");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
