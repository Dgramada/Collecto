package ss.protocols;

import ss.exceptions.ServerUnavailableException;

import java.net.ProtocolException;

public interface ClientProtocol {
  public void handleHello(String description) throws ServerUnavailableException, ProtocolException;

//  public void doHello();

  public void doLogin(String playername) throws ServerUnavailableException;

  public void doList() throws ServerUnavailableException;

  public void doQueue() throws ServerUnavailableException;

  public void doMove(String move) throws ServerUnavailableException;
}
