package com.tba.crane.support;

/**
 *
 * Configuration for the YardServer
 *
 * @author xiaoxue zheng
 * @version 1.0
 * @date 2020-10-26
 */
public class Configuration {

  /**
   *  Send the position to this ws connect url using websocket
   */
  private String positionReceiverUrl;

  public String getPositionReceiverUrl() {
    return positionReceiverUrl;
  }

  public Configuration setPositionReceiverUrl(String positionReceiverUrl) {
    this.positionReceiverUrl = positionReceiverUrl;
    return this;
  }
}
