package com.tba.crane;

import com.tba.crane.operation.OperatingCraneException;
import com.tba.crane.model.Track;
import com.tba.crane.support.Configuration;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TrackMoveCraneExceptionTest {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();
  private static Configuration configuration = new Configuration();
  {
    configuration.setPositionReceiverUrl("ws://localhost:"+8081+"/position");
  }
  Track track = new Track(1, 1, configuration);

  @Test
  public void test_move_not_exist_crane() {
    expectedException.expect(OperatingCraneException.class);
    track.moveCrane(1, 1);
  }

  @Test
  public void test_move_crane_out_lower_bound() {
    expectedException.expect(OperatingCraneException.class);
    track.instantiateCranes("1:1");
    track.moveCrane(1, 0);
  }

  @Test
  public void test_move_crane_out_upper_bound() {
    expectedException.expect(OperatingCraneException.class);
    track.instantiateCranes("1:1");
    track.moveCrane(1, 21);
  }

}
