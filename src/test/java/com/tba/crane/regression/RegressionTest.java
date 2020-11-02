package com.tba.crane.regression;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tba.crane.Main;
import com.tba.crane.operation.request.MoveCraneRequest;
import com.tba.crane.operation.request.MoveOutCraneRequest;
import com.tba.crane.operation.request.SetCraneRequest;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 1. Start the server
 * 2. Post request to the server/connect to the websocket
 * 3. Verify
 */
public class RegressionTest {

  private CloseableHttpClient httpClient = HttpClientBuilder.create().build();
  private ObjectMapper objectMapper = new ObjectMapper();

  @BeforeClass
  public static void before() throws InterruptedException {
    Main.main(new String[1]);
    Thread.sleep(10 * 1000);
  }

  @Test
  public void test_set_crane() throws IOException {
    HttpPost httpPost = new HttpPost("http://localhost:8081/crane/set");
    SetCraneRequest setCraneRequest = new SetCraneRequest();
    setCraneRequest.setCranes("1:1");
    String jsonString = objectMapper.writeValueAsString(setCraneRequest);
    StringEntity entity = new StringEntity(jsonString, "UTF-8");
    httpPost.setEntity(entity);
    httpPost.setHeader("Content-Type", "application/json;charset=utf8");
    CloseableHttpResponse response = httpClient.execute(httpPost);

    HttpEntity responseEntity = response.getEntity();
    Assert.assertEquals(200, response.getStatusLine().getStatusCode());
    Assert.assertEquals("{\"message\":\"success\"}", EntityUtils.toString(responseEntity));
  }

  @Test
  public void test_move_crane() throws IOException {
    HttpPost httpPost = new HttpPost("http://localhost:8081/crane/move");
    setCrane();

    MoveCraneRequest moveCraneRequest = new MoveCraneRequest();
    moveCraneRequest.setCraneId(1);
    moveCraneRequest.setNewPosition(2);

    String jsonString = objectMapper.writeValueAsString(moveCraneRequest);
    StringEntity entity = new StringEntity(jsonString, "UTF-8");
    httpPost.setEntity(entity);
    httpPost.setHeader("Content-Type", "application/json;charset=utf8");
    CloseableHttpResponse response = httpClient.execute(httpPost);

    HttpEntity responseEntity = response.getEntity();
    Assert.assertEquals(200, response.getStatusLine().getStatusCode());
    Assert.assertEquals("{\"message\":\"success\"}", EntityUtils.toString(responseEntity));

  }

  @Test
  public void test_move_out_crane() throws IOException, InterruptedException {
    setCrane();
    Thread.sleep(10000);
    MoveOutCraneRequest moveOutCraneRequest = new MoveOutCraneRequest();
    moveOutCraneRequest.setCraneId(1);

    String jsonString = objectMapper.writeValueAsString(moveOutCraneRequest);
    StringEntity entity = new StringEntity(jsonString, "UTF-8");
    HttpPost httpPost = new HttpPost("http://localhost:8081/crane/moveout");
    httpPost.setEntity(entity);
    httpPost.setHeader("Content-Type", "application/json;charset=utf8");
    CloseableHttpResponse response = httpClient.execute(httpPost);

    HttpEntity responseEntity = response.getEntity();
    Assert.assertEquals(200, response.getStatusLine().getStatusCode());
    Assert.assertEquals("{\"message\":\"success\"}", EntityUtils.toString(responseEntity));
  }

  @Test
  public void test_fail() throws IOException {
    HttpPost httpPost = new HttpPost("http://localhost:8081/crane/set");
    SetCraneRequest setCraneRequest = new SetCraneRequest();
    setCraneRequest.setCranes("abc");
    String jsonString = objectMapper.writeValueAsString(setCraneRequest);
    StringEntity entity = new StringEntity(jsonString, "UTF-8");
    httpPost.setEntity(entity);
    httpPost.setHeader("Content-Type", "application/json;charset=utf8");
    CloseableHttpResponse response = httpClient.execute(httpPost);

    HttpEntity responseEntity = response.getEntity();
    Assert.assertEquals(500, response.getStatusLine().getStatusCode());
    Assert.assertEquals("{\"message\":\"Please check the input format\"}",EntityUtils.toString(responseEntity));

  }

  @Test
  public void test_position_sender() throws URISyntaxException {
    String url = "ws://localhost:8082/crane";
    URI uri = new URI(url);
    WebSocketClient mWs = new WebSocketClient(uri) {
      @Override
      public void onOpen(ServerHandshake serverHandshake) {

      }

      @Override
      public void onMessage(String s) {
        Assert.assertEquals("connected",s);
      }

      @Override
      public void onClose(int i, String s, boolean b) {

      }

      @Override
      public void onError(Exception e) {
        Assert.assertEquals(2,1);
      }
    };
    mWs.connect();

  }


  private void setCrane() throws IOException {
    HttpPost httpPost = new HttpPost("http://localhost:8081/crane/set");
    SetCraneRequest setCraneRequest = new SetCraneRequest();
    setCraneRequest.setCranes("1:1");
    String jsonString = objectMapper.writeValueAsString(setCraneRequest);
    StringEntity entity = new StringEntity(jsonString, "UTF-8");
    httpPost.setEntity(entity);
    httpPost.setHeader("Content-Type", "application/json;charset=utf8");
    httpClient.execute(httpPost);
  }


}
