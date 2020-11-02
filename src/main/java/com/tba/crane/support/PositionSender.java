package com.tba.crane.support;

import com.tba.crane.model.Position;
import java.net.URI;
import java.net.URISyntaxException;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

/**
 * When crane position updated, The crane send out the message use WebSocketClient.</br>
 *
 * @author xiaoxue zheng
 * @version 1.0
 * @date 2020-10-26
 */
public class PositionSender {

  private String url;
  private WebSocketClient webSocketClient = null;

  public PositionSender(String url) throws URISyntaxException {
    this.url = url;
    try {
      webSocketClient = new WebSocketClient(new URI(url)) {
        @Override
        public void onOpen(ServerHandshake serverHandshake) {
          System.out.println("connected to the server");
        }

        @Override
        public void onMessage(String s) {

        }

        @Override
        public void onClose(int i, String s, boolean b) {

        }

        @Override
        public void onError(Exception e) {
        }
      };
      webSocketClient.connect();
    }catch (Exception e){
      e.printStackTrace();
    }

  }

  public void sendPosition(int craneId, Position position) {
    try {
      webSocketClient.send(String.format("%s:%s:%s", craneId, position.getX(), position.getY()));
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  public void stop() {

  }
}
