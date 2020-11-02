package com.tba.crane;

import com.tba.crane.operation.OperatingCraneException;
import com.tba.crane.model.Track;
import com.tba.crane.support.Configuration;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TrackSetCraneExceptionTest {
  @Rule
  public ExpectedException expectedException = ExpectedException.none();
  private static Configuration configuration = new Configuration();
  {
    configuration.setPositionReceiverUrl("ws://localhost:"+8081+"/position");
  }
  private Track track = new Track(1, 1, configuration);

  @Test
  public void test_set_null(){
    track.instantiateCranes(null);
  }

  @Test
  public void test_set_bad_format_input(){
    expectedException.expect(OperatingCraneException.class);
    expectedException.expectMessage("Please check the input format");
    track.instantiateCranes("ab");
  }

  @Test
  public void test_set_out_track_position_lower_bound_input(){
    expectedException.expect(OperatingCraneException.class);
    expectedException.expectMessage("Can not set cranes, please check the input");
    track.instantiateCranes("1:0");
  }

  @Test
  public void test_set_out_track_position_upper_bound_input(){
    expectedException.expect(OperatingCraneException.class);
    expectedException.expectMessage("Can not set cranes, please check the input");

    track.instantiateCranes("1:21");
  }

  @Test
  public void test_set_occupied_track_position(){
    expectedException.expect(OperatingCraneException.class);
    expectedException.expectMessage("Can not set cranes, please check the input");

    track.instantiateCranes("1:1");
    track.instantiateCranes("2:1");
  }

  @Test
  public void test_set_already_on_the_track_crane(){
    expectedException.expect(OperatingCraneException.class);
    expectedException.expectMessage("Can not set cranes, please check the input");

    track.instantiateCranes("1:1");
    track.instantiateCranes("1:2");
  }

}
