package com.tba.crane.operation;

/**
 *
 * When operating cranes fail, throw this exception 
 *
 * @author xiaoxue zheng
 * @version 1.0
 * @date 2020-10-26
 */
public class OperatingCraneException extends RuntimeException
{
    public OperatingCraneException(String message){
        super(message);
    }

    public OperatingCraneException(Exception e){
        super(e);
    }
}
