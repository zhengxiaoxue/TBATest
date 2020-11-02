package com.tba.crane;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tba.crane.model.Track;
import com.tba.crane.operation.OperatingCraneException;
import com.tba.crane.operation.OperationHandler;
import com.tba.crane.operation.request.MoveCraneRequest;
import com.tba.crane.operation.request.MoveOutCraneRequest;
import com.tba.crane.operation.request.SetCraneRequest;
import com.tba.crane.support.Configuration;
import io.undertow.io.Sender;
import io.undertow.server.HttpServerExchange;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(MockitoJUnitRunner.class)
public class OperationHandlerTest {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();
  private ObjectMapper objectMapper = new ObjectMapper();
  private static Configuration configuration = new Configuration();
  private Track track = new Track(1, 1, configuration);

  private OperationHandler operationHandler = new OperationHandler(track);

  @Mock
  HttpServerExchange exchange;
  @Mock
  Sender sender;

  @Test
  public void test_handle_bad_path_request() {
    expectedException.expect(OperatingCraneException.class);
    when(exchange.getRelativePath()).thenReturn("/****");
    operationHandler.handleRequest(exchange);
  }

  @Test
  public void test_handle_not_supported_request() {
    expectedException.expect(OperatingCraneException.class);
    when(exchange.getRelativePath()).thenReturn("/crane/***");
    operationHandler.handleRequest(exchange);
  }

  @Test
  public void test_handle_not_supported_request2() {
    expectedException.expect(OperatingCraneException.class);
    when(exchange.getRelativePath()).thenReturn("/crane/set/***");
    operationHandler.handleRequest(exchange);
  }

  @Test
  public void test_handle_set_crane_request() throws JsonProcessingException {
    when(exchange.getRelativePath()).thenReturn("/crane/set");
    SetCraneRequest setCraneRequest = new SetCraneRequest();
    setCraneRequest.setCranes("1:1");
    when(exchange.getInputStream()).thenReturn(IOUtils.toInputStream(objectMapper.writeValueAsString(setCraneRequest)));
    when(exchange.getResponseSender()).thenReturn(sender);
    operationHandler.handleRequest(exchange);
    Assert.assertEquals(1, track.getCrane(1).getId());
  }

  @Test
  public void test_handle_move_crane_request() throws JsonProcessingException {
    when(exchange.getRelativePath()).thenReturn("/crane/move");
    track.instantiateCranes("1:1");
    MoveCraneRequest moveCraneRequest = new MoveCraneRequest();
    moveCraneRequest.setCraneId(1);
    moveCraneRequest.setNewPosition(2);
    when(exchange.getInputStream()).thenReturn(IOUtils.toInputStream(objectMapper.writeValueAsString(moveCraneRequest)));
    when(exchange.getResponseSender()).thenReturn(sender);
    operationHandler.handleRequest(exchange);
    Assert.assertEquals(1, track.getCrane(2).getId());
  }

  @Test
  public void test_handle_move_out_crane_request() throws JsonProcessingException {
    when(exchange.getRelativePath()).thenReturn("/crane/moveout");
    track.instantiateCranes("1:1");
    MoveOutCraneRequest moveOutCraneRequest = new MoveOutCraneRequest();
    moveOutCraneRequest.setCraneId(1);
    when(exchange.getInputStream()).thenReturn(IOUtils.toInputStream(objectMapper.writeValueAsString(moveOutCraneRequest)));
    when(exchange.getResponseSender()).thenReturn(sender);
    operationHandler.handleRequest(exchange);
    Assert.assertEquals(track.getRunningCraneCnt(),0);
  }

}
