package com.tba.crane;

import com.tba.crane.model.Track;
import com.tba.crane.support.Configuration;
import org.junit.Assert;
import org.junit.Test;

public class TrackTest {

  private static Configuration configuration = new Configuration();

  Track track = new Track(1, 1, configuration);

  @Test
  public void test__set_crane() {
    track.instantiateCranes("1:1");
    track.instantiateCranes("2:2");
    Assert.assertEquals(1, track.getCrane(1).getId());
    Assert.assertEquals(2, track.getCrane(2).getId());
    Assert.assertEquals(track.getRunningCraneCnt(), 2);
  }

  @Test
  public void test__ge_crane_positions() {
    track.instantiateCranes("1:1");
    track.instantiateCranes("2:2");
    Assert.assertEquals(1, track.getCrane(1).getId());
    Assert.assertEquals(2, track.getCrane(2).getId());
    Assert.assertEquals(track.getRunningCraneCnt(), 2);
    Assert.assertEquals("1:1:1,2:1:2", track.getCranePositions());
  }

  @Test
  public void test_simple_move_crane() {
    track.instantiateCranes("1:1");
    track.moveCrane(1, 2);
    Assert.assertEquals(1, track.getCrane(2).getId());
    Assert.assertNull(track.getCrane(1));
    Assert.assertEquals(1, track.getRunningCraneCnt());
    Assert.assertEquals(1, (int)track.getCraneIdToPositionMap().get(1));
  }

  @Test
  public void test_move_crane_when_blocked_right_side() {
    track.instantiateCranes("1:1,2:2");
    //2 block in the way
    track.moveCrane(1, 3);
    Assert.assertEquals(1, track.getCrane(2).getId());
    Assert.assertEquals(2, track.getCrane(3).getId());
    Assert.assertEquals(2, track.getRunningCraneCnt());
    Assert.assertEquals(1, (int)track.getCraneIdToPositionMap().get(1));
    Assert.assertEquals(2, (int)track.getCraneIdToPositionMap().get(2));
  }


  @Test
  public void test_move_crane_when_blocked_left_side() {
    track.instantiateCranes("2:2,3:3");
    //2 block in the way
    track.moveCrane(3, 1);
    Assert.assertEquals(2, track.getCrane(1).getId());
    Assert.assertEquals(3, track.getCrane(2).getId());
    Assert.assertEquals(track.getRunningCraneCnt(), 2);
    Assert.assertEquals(0, (int)track.getCraneIdToPositionMap().get(2));
    Assert.assertEquals(1, (int)track.getCraneIdToPositionMap().get(3));
  }

  @Test
  public void test_simple_move_out_crane() {
    track.instantiateCranes("1:1");
    track.moveOutCrane(1);
    Assert.assertNull(track.getCrane(1));
    Assert.assertEquals(0, track.getRunningCraneCnt());
  }

}
