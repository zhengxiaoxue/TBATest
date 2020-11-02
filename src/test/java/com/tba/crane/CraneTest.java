package com.tba.crane;

import com.tba.crane.model.Crane;
import com.tba.crane.model.Position;
import org.junit.Assert;
import org.junit.Test;

public class CraneTest {

  @Test
  public void testTerminate(){
    Crane crane = new Crane(1, new Position(1,1));
    new Thread(crane).start();
    crane.terminate();
    Assert.assertTrue(crane.isTerminated());
  }

}
