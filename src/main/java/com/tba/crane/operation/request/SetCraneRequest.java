package com.tba.crane.operation.request;


/**
 *
 * The request body is used to set cranes
 *
 * @author xiaoxue zheng
 * @version 1.0
 * @date 2020-10-26
 */
public class SetCraneRequest implements OperationRequest
{
    private String cranes;

    public SetCraneRequest(){

    }


    public String getCranes()
    {
        return cranes;
    }


    public void setCranes(String cranes)
    {
        this.cranes = cranes;
    }


    @Override public String toString()
    {
        return "InitiateCraneRequest{" +
            "cranes=" + cranes +
            '}';
    }
}
