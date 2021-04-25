package ss.client;

import ss.exceptions.InvalidMoveException;
import ss.exceptions.JoinGameException;
import ss.exceptions.ServerUnavailableException;
import ss.gamedesign.Board;
import ss.gamedesign.Colour;
import ss.gamedesign.Move;
import ss.gamedesign.NetworkGame;
import ss.protocols.ClientProtocol;
import ss.protocols.ProtocolMessages;
import ss.server.CollectoClientHandler;
import ss.server.CollectoServer;
import ss.utils.TextIO;

import java.io.*;
import java.net.InetAddress;
import java.net.ProtocolException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class CollectoClient implements Runnable, ClientProtocol {
  private int port = 0;
  private InetAddress address;
  private String host;
  private static String playerName;
  static Socket socket;
  static BufferedReader in;
  static BufferedWriter out;
  //Board board;
  NetworkGame game;

  CollectoClientTUI view;

  public CollectoClient() {
    view = new CollectoClientTUI(this);
  }

  @Override
  public void run() {
    Boolean connectServer = true;
    while (connectServer) {
      createConnection();

      try {
        handleHello(view.getString("Input user name: "));
        doLogin(view.getString("Input player name: "));
      } catch (ServerUnavailableException e) {
        e.printStackTrace();
      } catch (ProtocolException e) {
        e.printStackTrace();
      }
      while (true) {
        try {
          view.start();
        } catch (ServerUnavailableException e) {
          closeConnection();
        }
        try {
          handleReply();
          //handleNewGame();
          //handleMove();
        } catch (ProtocolException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (ServerUnavailableException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
//      connectServer = view.getBoolean("if you want to connect to server?");
    }
    closeConnection();
  }


  public void handleMove() throws ServerUnavailableException {
    String reply = readLineFromServer();
    System.out.println(reply);
  }

  public void createConnection() {
    clearConnection();
    while (socket == null) {
      System.out.println("Input your name: ");
      playerName = TextIO.getln();
      System.out.println("Input the host: ");
      host = TextIO.getln();
      System.out.println("Input the port: ");
      port = TextIO.getInt();

      try {
        address = InetAddress.getByName(host);
        System.out.println("Connecting to the port " + port + " and address " + address);
        socket = new Socket(address, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        System.out.println("Input your command: ");

        String command = TextIO.getln();
        out.write(command + playerName);

        out.newLine();
        out.flush();
        String line = in.readLine();
        System.out.println(line);
//        while (line != null && !line.equals("")) {
//          System.out.println(line);
//          line = in.readLine();
//        }
      } catch (IOException e) {
        System.out.println("Can not connect to the port " + port);
      }
    }
  }

  public void clearConnection() {
    port = 0;
    address = null;
    host = null;
    playerName = null;
    socket = null;
  }

  public void closeConnection() {
    try {
      in.close();
      out.close();
      socket.close();
    } catch (IOException e) {
      System.out.println("fail close");
    }
  }

//  public void start() {
//    boolean connectToNewServer = true;
//    while (connectToNewServer) {
//      //Ask for IP and Port, attempt to connnect, try again if unsuccessful
//      try {
//        createConnection();
//        try {
//          view.printHelpMenu();
//          view.start();
//        } catch (ServerUnavailableException e) {
//          view.showMessage("The protocol was not respected." + "Try connecting to a new server.");
//          closeConnection();
//        }
//        connectToNewServer = view.getBoolean("Do you want to connect to a new server?");
//      } catch (Exception e1) {
//        connectToNewServer = false;
//      }
//    }
//    view.showMessage("See you later!");
//  }

  public static void main(String[] args) throws IOException {
    CollectoClient client = new CollectoClient();
    Thread a = new Thread(client);
    a.start();
  }

  /**
   * Sends a message to the connected server, followed by a new line.
   * The stream is then flushed.
   *
   * @param msg the message to write to the OutputStream.
   * @throws ServerUnavailableException if IO errors occur.
   */
  public synchronized void sendMessage(String msg)
          throws ServerUnavailableException {
    if (out != null) {
      try {
        out.write(msg);
        out.newLine();
        out.flush();
      } catch (IOException e) {
        System.out.println(e.getMessage());
        throw new ServerUnavailableException("Could not write "
                + "to server.");
      }
    } else {
      throw new ServerUnavailableException("Could not write "
              + "to server.");
    }
  }

  /**
   * Reads and returns one line from the server.
   *
   * @return the line sent by the server.
   * @throws ServerUnavailableException if IO errors occur.
   */
  public String readLineFromServer()
          throws ServerUnavailableException {
    if (in != null) {
      try {
        // Read and return answer from Server
        String answer = in.readLine();
        if (answer == null) {
          throw new ServerUnavailableException("Could not read "
                  + "from server.");
        }
        return answer;
      } catch (IOException e) {
        throw new ServerUnavailableException("Could not read "
                + "from server.");
      }
    } else {
      throw new ServerUnavailableException("Could not read "
              + "from server.");
    }
  }

  @Override
  public void handleHello(String description) throws ServerUnavailableException, ProtocolException {
    String msg = ProtocolMessages.HELLO + ProtocolMessages.DELIMITER + description;
    sendMessage(msg);
    String reply = readLineFromServer();
//    String temp = reply.split(ProtocolMessages.DELIMITER);
    if (reply.contains(ProtocolMessages.HELLO + ProtocolMessages.DELIMITER)) {
      String[] temp = reply.split(ProtocolMessages.DELIMITER);
      System.out.println("Welcome to the Collecto: " + temp[1]);
    } else {
      throw new ProtocolException("Doesn't adhere to the protocol");
    }
  }

  public String handleNewGame(String reply) throws ServerUnavailableException, ProtocolException {
    //String reply = readLineFromServer();
    Board board;
    //  if(reply.contains((ProtocolMessages.NEWGAME + ProtocolMessages.DELIMITER))) {

    String[] split = reply.split("~");
    /*
    board = new Board();
    for (int i = 1; i < split.length - 2; i++) {
      Colour color = Colour.EMPTY;
      switch (Integer.parseInt(split[i])) {
        case 1:
          color = Colour.BLUE;
          break;
        case 2:
          color = Colour.YELLOW;
          break;
        case 3:
          color = Colour.RED;
          break;
        case 4:
          color = Colour.ORANGE;
          break;
        case 5:
          color = Colour.PURPLE;
          break;
        case 6:
          color = Colour.GREEN;
          break;
      }
      board.setField(color, i-1);     
    }
    */
    board = getBoardByString(reply);
    Board originalBoard = getBoardByString(reply);
    game = new NetworkGame("test", 2);

    CollectoServer s = new CollectoServer();
    CollectoClientHandler handlera = new CollectoClientHandler(socket, s, "aa");
    CollectoClientHandler handlerb = new CollectoClientHandler(socket, s, "bb");
    try {
      game.join("aa", handlera);
      game.join("bb", handlerb);
    } catch (JoinGameException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    //System.out.println(originalBoard.toString());

    Move m = new Move(board);
    m.setBoard(originalBoard);
    game.setMove(m);

    game.setBoard(originalBoard);
    System.out.println(game.getBoard().toString());
    //System.out.println(board.toString());
    return split[split.length - 2];
  }

  public Board getBoardByString(String s) {
    Board board = new Board();
    String[] split = s.split("~");
    for (int i = 1; i < split.length - 2; i++) {
      Colour color = Colour.EMPTY;
      switch (Integer.parseInt(split[i])) {
        case 1:
          color = Colour.BLUE;
          break;
        case 2:
          color = Colour.YELLOW;
          break;
        case 3:
          color = Colour.RED;
          break;
        case 4:
          color = Colour.ORANGE;
          break;
        case 5:
          color = Colour.PURPLE;
          break;
        case 6:
          color = Colour.GREEN;
          break;
      }
      board.setField(color, i - 1);
    }
    return board;
  }

  public void handleReply() throws ServerUnavailableException, ProtocolException {
    while (true) {
      String reply = readLineFromServer();
      if (reply.contains((ProtocolMessages.NEWGAME + ProtocolMessages.DELIMITER))) {
        String player1Name = handleNewGame(reply);

        //if (player1Name.equals(playerName)) {
        break;
        //}
      } else if (reply.contains((ProtocolMessages.MOVE + ProtocolMessages.DELIMITER))) {
        //System.out.println(reply);
        if (game != null) {
          String[] move = Arrays.copyOfRange(reply.split("~"), 1, reply.split("~").length);
          try {
            game.move(move);
          } catch (InvalidMoveException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          System.out.println(game.getBoard().toString());
        }
        break;
      }
    }
  }

  public void handleNewGame() throws ServerUnavailableException, ProtocolException {
    String reply = readLineFromServer();
//    Board board;
    if (reply.contains((ProtocolMessages.NEWGAME + ProtocolMessages.DELIMITER))) {
      String[] split = reply.split("~");
      Board board = new Board();
      for (int i = 1; i < split.length - 2; i++) {
        Colour color = Colour.EMPTY;
        switch (Integer.parseInt(split[i])) {
          case 1:
            color = Colour.BLUE;
            break;
          case 2:
            color = Colour.YELLOW;
            break;
          case 3:
            color = Colour.RED;
            break;
          case 4:
            color = Colour.ORANGE;
            break;
          case 5:
            color = Colour.PURPLE;
            break;
          case 6:
            color = Colour.GREEN;
            break;
        }
        board.setField(color, i - 1);

      }
      System.out.println(board.toString());
    } else {
      //throw new ProtocolException("Doesn't adhere to the protocol");
    }

  }


  @Override
  public void doLogin(String playerName) throws ServerUnavailableException {
    String msg = ProtocolMessages.LOGIN + ProtocolMessages.DELIMITER + playerName;
    sendMessage(msg);
    System.out.println(readLineFromServer());

  }

  @Override
  public void doList() throws ServerUnavailableException {
    sendMessage(ProtocolMessages.LIST);
    System.out.println(readLineFromServer());
  }

  @Override
  public void doQueue() throws ServerUnavailableException {
    sendMessage(ProtocolMessages.QUEUE);
    System.out.println(readLineFromServer());
  }

  @Override
  public void doMove(String move) throws ServerUnavailableException {
    String msg = ProtocolMessages.MOVE + ProtocolMessages.DELIMITER + move;
    sendMessage(msg);
    System.out.println(readLineFromServer());
  }

}
