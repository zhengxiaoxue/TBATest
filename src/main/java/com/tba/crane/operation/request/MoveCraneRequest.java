package com.tba.crane.operation.request;

/**
 *
 * The request body is used to move cranes
 *
 * @author xiaoxue zheng
 * @version 1.0
 * @date 2020-10-26
 */
public class MoveCraneRequest implements OperationRequest{
  private int craneId;
  private int newPosition;

  public int getCraneId() {
    return craneId;
  }

  public int getNewPosition() {
    return newPosition;
  }

  public void setCraneId(int craneId) {
    this.craneId = craneId;
  }

  public void setNewPosition(int newPosition) {
    this.newPosition = newPosition;
  }
}
