package com.tba.crane.model;

import com.tba.crane.operation.OperatingCraneException;
import com.tba.crane.support.Configuration;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cranes can only move along the track, all the cranes are same, it has two parking positions for instantiating crane. A track belongs to a
 * yard.
 * For instantiation and moving out we can directly operating instead of moving other crane.
 * For move we suppose all cranes are same so we operate the crane  to the left or the right rather than move out then move in
 * Example:
 * We use craneId:position1 to represent crane with craneId is at positon1
 * 1:1,2:2 after move 1 to position 3 it become 1:2,2:3
 * 1:2,2:6 after move 2 to position 1 it become 1:1,2:2
 * @author xiaoxue zheng
 * @version 1.0
 * @date 2020-10-26
 */
public class Track {

  private static final int DEFAULT_POSITIONS = 20;
  private static final int OUT_TRACK_POSITION = -1;

  /**
   * the is of the track
   */
  private int trackId;

  /**
   * We suppose track is horizontal level, it means the cranes run on this have the same x equals positionX
   */
  private int positionX;

  /**
   * The number fo slots the track has used to set crane
   */
  private int positions;

  /**
   * The cranes run on this track
   */
  private Crane[] cranes;

  /**
   * The configuration of the Yard
   */
  private Configuration configuration;

  /**
   * The key is the crane id and the value is the position;
   */
  public Map<Integer, Integer> craneIdToPositionMap;

  public Track(int trackId, int positionX, int positions, Configuration configuration) {
    this.trackId = trackId;
    this.positions = positions;
    this.positionX = positionX;
    cranes = new Crane[this.positions];
    this.configuration = configuration;
    craneIdToPositionMap = new HashMap<Integer, Integer>();
  }

  public Track(int trackId, int positionX, Configuration configuration) {
    this(trackId, positionX, DEFAULT_POSITIONS, configuration);
  }

  /**
   * we suppose when we instantiate the crane, we don't have to move other crane even throw the new  crane</br> is set between two cranes
   *
   * @param craneStr format: craneId1:position1,craneId2:position2
   * @throws OperatingCraneException if the position is out bound or is occupied or the crane already there throw exception
   */
  public void instantiateCranes(String craneStr) throws OperatingCraneException {
    System.out.println("instantiate crane" + craneStr);
      if (StringUtils.isEmpty(craneStr)) {
          return;
      }

    List<List<Integer>> newCranes = formatSetCraneParam(craneStr);
    if (!canSetCranes(newCranes)) {
      throw new OperatingCraneException("Can not set cranes, please check the input");
    }
    for (List<Integer> newCrane : newCranes) {
      int craneId = newCrane.get(0);
      Crane crane = new Crane(craneId, this, new Position(positionX, newCrane.get(1)));
      crane.registerPositionSender(configuration.getPositionReceiverUrl());
      int slot = newCrane.get(1)-1;
      cranes[slot] = crane;
      craneIdToPositionMap.put(craneId, slot);
      new Thread(crane).start();
    }
  }


  /**
   * We suppose all cranes are same, so when moving happens, cranes move along the track to the left or the right</br>
   * based on where the new position is.
   * Example:  1:1,2:2 after move 1 to 3 it become 1:2,2:3
   *  1:2,2:6 after move 2 to position 1 it become 1:1,2:2
   *
   * @param craneId
   * @param newPosition
   * @throws OperatingCraneException
   */
  public void moveCrane(int craneId, int newPosition) throws OperatingCraneException {
    System.out.println("move crane " + craneId + " new position " + newPosition);

    //the UI start from 1
    newPosition = newPosition - 1;
    if (!canMoveCrane(craneId, newPosition)) {
      throw new OperatingCraneException("Can not move crane " + craneId + " to " + newPosition);
    }
    int oldPosition = craneIdToPositionMap.get(craneId);
    int lastNotNullIndex = -1;
    if (newPosition > oldPosition) {
      lastNotNullIndex = newPosition;
      for (int i = newPosition -1 ; i >= oldPosition; i--) {
        if (cranes[i] == null) {
          continue;
        }
        craneIdToPositionMap.put(cranes[i].getId(), lastNotNullIndex);
        cranes[lastNotNullIndex] = cranes[i];
        cranes[i].setPosition(new Position(positionX, lastNotNullIndex+1));
        lastNotNullIndex = i;
      }
    } else {
      lastNotNullIndex = newPosition;
      for (int i = newPosition + 1; i <= oldPosition; i++) {
        if (cranes == null) {
          continue;
        }
        craneIdToPositionMap.put(cranes[i].getId(), lastNotNullIndex);
        cranes[lastNotNullIndex] = cranes[i];
        cranes[i].setPosition(new Position(positionX, lastNotNullIndex+1));
        lastNotNullIndex = i;
      }
    }
    if(oldPosition != newPosition){
      cranes[oldPosition] = null;
    }
  }

  /**
   * We suppose crane can be moved out the track directyly
   * @param craneId
   */
  public void moveOutCrane(int craneId) {
    System.out.println("move out crane " + craneId);

    if (!canMoveOutCrane(craneId)) {
      throw new OperatingCraneException("Can not move the crane, it doesn't run on this track");
    }
    int position = craneIdToPositionMap.get(craneId);
    Crane crane = cranes[position];
    crane.terminate();
    craneIdToPositionMap.remove(crane.id);
    cranes[position] = null;
  }

  public String getCranePositions(){
    StringBuilder ans = new StringBuilder();
    for(Crane crane: cranes){
      if(null != crane){
        ans.append(",");
        ans.append(crane.getId());
        ans.append(":");
        ans.append(crane.getPosition().getX());
        ans.append(":");
        ans.append(crane.getPosition().getY());
      }
    }
    return ans.length()==0?ans.toString():ans.toString().substring(1);
  }

  private boolean canMoveOutCrane(int craneId) {
    return null != craneIdToPositionMap.get(craneId);
  }


  private boolean canSetCranes(List<List<Integer>> newCranes) {
    if (null == newCranes) {
      return false;
    }
    for (List<Integer> crane : newCranes) {
      // position must be validate
      int p = crane.get(1);
      if (p < 1 || p > positions) {
        return false;
      }
      // the position not empty
      if (cranes[p-1] != null) {
        return false;
      }
      // the crane already on the track
      if (craneIdToPositionMap.get(crane.get(0)) != null) {
        return false;
      }
    }
    return true;
  }


  private List<List<Integer>> formatSetCraneParam(String craneStr) {
    List<List<Integer>> newCranes = new ArrayList<List<Integer>>();
    try {
      String[] arr = craneStr.split(",");
      for (String str : arr) {
        List<Integer> list = new ArrayList<Integer>();
        list.add(Integer.parseInt(str.substring(0, str.indexOf(":"))));
        list.add(Integer.parseInt(str.substring(str.indexOf(":") + 1)));
        newCranes.add(list);
      }
      return newCranes;
    } catch (Exception e) {
      throw new OperatingCraneException("Please check the input format");
    }

  }

  private boolean canMoveCrane(int craneId, int newPosition) {
    if (newPosition < 0 || newPosition >= positions) {
      return false;
    }
    if (craneIdToPositionMap.get(craneId) == null) {
      return false;
    }
    return true;
  }

  public Crane getCrane(int position) {
    return cranes[position - 1];
  }

  public int getRunningCraneCnt() {
    int cnt = 0;
    for (Crane crane : cranes) {
      if (null != crane) {
        cnt++;
      }
    }
    return cnt;
  }

  public Map<Integer, Integer> getCraneIdToPositionMap() {
    return craneIdToPositionMap;
  }
}
