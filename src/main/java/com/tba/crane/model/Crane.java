package com.tba.crane.model;

/**
 * Cranes run on the track, and can not pass each other after instantiation</br>
 *
 *
 * @author xiaoxue zheng
 * @version 1.0
 * @date 2020-10-26
 */
public class Crane extends Vehicle {

  private Track track;

  public Crane(int id, Position position) {
    super(id, position);
  }

  public Crane(int id, Track track, Position position) {
    super(id, position);
    this.track = track;
  }

  @Override
  public String toString() {
    return "Crane{" +
        "id=" + id +
        ", track=" + track +
        ", position=" + position +
        ", terminated=" + terminated +
        '}';
  }


}
