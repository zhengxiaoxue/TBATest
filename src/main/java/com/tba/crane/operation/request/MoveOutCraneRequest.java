package com.tba.crane.operation.request;

/**
 *
 * The request body is used to move out crane
 *
 * @author xiaoxue zheng
 * @version 1.0
 * @date 2020-10-26
 */
public class MoveOutCraneRequest implements OperationRequest{
  private int craneId;

  public int getCraneId() {
    return craneId;
  }

  public void setCraneId(int craneId){
    this.craneId = craneId;
  }
}
