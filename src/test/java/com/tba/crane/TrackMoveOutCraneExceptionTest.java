package com.tba.crane;

import com.tba.crane.operation.OperatingCraneException;
import com.tba.crane.model.Track;
import com.tba.crane.support.Configuration;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TrackMoveOutCraneExceptionTest {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();
  private static Configuration configuration = new Configuration();
  {
    configuration.setPositionReceiverUrl("ws://localhost:"+8081+"/position");
  }
  private Track track = new Track(1, 1, configuration);

  @Test
  public void test_move_out_crane_not_running_on_truck(){
    expectedException.expect(OperatingCraneException.class);
    expectedException.expectMessage("Can not move the crane, it doesn't run on this track");
    track.moveOutCrane(1);
  }
}
