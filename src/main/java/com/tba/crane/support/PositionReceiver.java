package com.tba.crane.support;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfinal.aop.Aop;
import java.io.IOException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * When crane position updated, send out the message use websocket.</br>
 * Then the position receiver will receive the message and handle it by onMessage
 *
 * @author xiaoxue zheng
 * @version 1.0
 * @date 2020-10-26
 */
@ServerEndpoint("/position")
public class PositionReceiver {
  private ObjectMapper mapper = new ObjectMapper();
  @OnOpen
  public void onOpen(Session session) throws IOException {
    System.out.println(session.getId()+" connected");
  }

  @OnClose
  public void onClose(Session session) throws IOException {
    System.out.println(session.getId()+" closed");
  }

  @OnMessage
  public void onMessage(String message) throws IOException {
    Aop.get(PositionDispatcher.class).sendPosition(message);
  }

  @OnError
  public void onError(Session session, Throwable error) {
    error.printStackTrace();
  }

}
