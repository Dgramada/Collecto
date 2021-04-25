package ss.client;

import ss.client.CollectoClient;
import ss.client.CollectoClientView;
import ss.exceptions.ExitProgram;
import ss.exceptions.ServerUnavailableException;
import ss.gamedesign.Collecto;
import ss.protocols.ProtocolMessages;
import ss.utils.TextIO;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ProtocolException;
import java.net.UnknownHostException;

/**
 * TUI for the Collecto Client, implementing the
 * CollectoClientView interface.
 *
 * @author Yuchen Sun & Yordan Tsintsov
 * @throws ServerUnavailableException
 */

public class CollectoClientTUI implements CollectoClientView {
  private CollectoClient client;
  private PrintWriter console;

  public CollectoClientTUI (CollectoClient client){
    this.client= client;
    console = new PrintWriter(System.out,true);
  }
  @Override
  public void start() throws ServerUnavailableException {
    //Ask for user input continously and handle communication accordingly
    boolean exit = false;
    //while (!exit){
      String input = TextIO.getlnString();
      try{
        handleUserInput(input);
      }catch (ExitProgram | ProtocolException e){
        exit = true;
//        client.sendExit();
        client.closeConnection();
      }
   // }
  }

  @Override
  public void handleUserInput(String input) throws ExitProgram, ServerUnavailableException, ProtocolException {
    String[] splitted = input.split("~"); //split on space

    String commandString = splitted[0];
    String cmd1 = null;
//    String cmd2 = null;

    if(splitted.length > 1){
      cmd1 = splitted[1];
//      cmd2 = splitted[2];
    }
//    String cmd = input;
//    if(commandString.length()==1){

      switch (commandString){
        case ProtocolMessages.HELLO:
          client.handleHello(cmd1);
          break;

        case ProtocolMessages.LOGIN:
          client.doLogin(cmd1);
          break;

        case ProtocolMessages.LIST:
          client.doList();
          break;

        case ProtocolMessages.QUEUE:
          client.doQueue();
          break;

        case ProtocolMessages.MOVE:
          client.doMove(cmd1);
          break;

        case ProtocolMessages.NEWGAME:
          client.handleNewGame(input);
          break;

        default:
          System.out.println("Unknow command: " + commandString);
          printHelpMenu();
      }
//    }else{
//      System.out.println("Unknow command: " + commandString);
//      printHelpMenu();
//    }

  }

  @Override
  public void showMessage(String message) {
    console.println(message);
  }

  @Override
  public InetAddress getIp() {
    InetAddress addr = null;
    while(addr == null){
      String input = getString("Enter the IP or host to connecct to");
      try{
        addr = InetAddress.getByName(input);
      }catch (UnknownHostException e){
        showMessage("ERROR: host " + input + " unknown");
      }
    }
    return addr;
  }

  @Override
  public String getString(String question) {
    showMessage(question);
    return TextIO.getlnString();
  }

  @Override
  public int getInt(String question) {
    showMessage(question);
    return TextIO.getlnInt();
  }

  @Override
  public boolean getBoolean(String question) {
    showMessage(question);
    return TextIO.getBoolean();
  }

  @Override
  public void printHelpMenu() {
    System.out.println("you can input these commands:HELLO,LOGIN,LIST,QUEUE,MOVE. Try again:");
  }

}
