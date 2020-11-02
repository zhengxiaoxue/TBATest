package com.tba.crane.support;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/monitor")
public class PositionDispatcher {
  private static Map<String, Session> clients;

  public PositionDispatcher() {
    clients = new ConcurrentHashMap<String, Session>();
  }

  @OnOpen
  public void onOpen(Session session) throws IOException {
    System.out.println(session.getId()+" connected");
    clients.put(session.getId(), session);
  }

  @OnClose
  public void onClose(Session session) throws IOException {
    System.out.println(session.getId()+" closed");
    clients.remove(session.getId(), session);
  }

  @OnMessage
  public void onMessage(String message) throws IOException {
    System.out.println(message+" received");
  }

  @OnError
  public void onError(Session session, Throwable error) {
    error.printStackTrace();
    clients.remove(session.getId());
  }

  public void sendPosition(String positionMessage) {
    for (Map.Entry<String, Session> client : clients.entrySet()) {
      try {
        client.getValue().getAsyncRemote().sendText(positionMessage);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
