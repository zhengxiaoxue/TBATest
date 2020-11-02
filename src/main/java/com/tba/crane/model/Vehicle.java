package com.tba.crane.model;

import com.tba.crane.support.PositionSender;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

/**
 * run in separate process
 * @author xiaoxue zheng
 * @version 1.0
 * @date 2020-10-26
 */
public abstract class Vehicle implements Runnable {

  protected int id;
  protected Position position;
  protected PositionSender positionSender;

  protected volatile boolean terminated;

  public Vehicle(int id, Position position) {
    this.id = id;
    this.position = position;
  }

  public Position getPosition() {
    return position;
  }

  public Vehicle setPosition(Position position) {
    this.position = position;
    return this;
  }

  public void resume() {
    //TODO we don't have resume mechanism now, we recycle directly
    terminated = false;
  }

  public void terminate() {
    terminated = true;
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      //ignore
    }
    if(null != positionSender){
      positionSender.sendPosition(id, new Position(-1, -1));
    }
    unregisterPositionSender();
  }

  public boolean isTerminated() {
    return terminated;
  }

  public void registerPositionSender(String positionReceiverUrl) {
    try {
      if(null != positionReceiverUrl){
        this.positionSender = new PositionSender(positionReceiverUrl);
      }
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
  }

  public void unregisterPositionSender() {
    if (null != positionSender) {
      this.positionSender.stop();
    }
  }


  /**
   * Send position message in period when running
   */
  @Override
  public void run() {
    while (!terminated) {
      try {
        Thread.sleep(TimeUnit.SECONDS.toMillis(1));
        if(positionSender!=null){
          positionSender.sendPosition(id, position);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public String toString() {
    return "Vehicle{" +
        "id=" + id +
        ", position=" + position +
        ", terminated=" + terminated +
        '}';
  }

  @Override
  public int hashCode() {
    return id;
  }

  public int getId() {
    return id;
  }

}
