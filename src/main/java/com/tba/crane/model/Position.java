package com.tba.crane.model;

/**
 * The position of the crane, actually for crane, x is the track's positionX, y is the slot of the track
 */
public class Position {

  private int x;
  private int y;

  public Position(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }
}

