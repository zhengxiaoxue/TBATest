package com.tba.crane;

import com.tba.crane.model.Track;
import com.tba.crane.operation.OperationHandler;
import com.tba.crane.support.Configuration;
import com.tba.crane.support.YardServer;

/**
 *
 * The server for performing operations from user and helping receive messages and dispatch message from cranes
 *
 * @author xiaoxue zheng
 * @version 1.0
 * @date 2020-10-26
 */
public class Main
{

    private static int DEFAULT_PORT = 8081;
    private static int WEBSOCKET_DEFAULT_PORT = 8082;

    public static void main(String[] args)
    {
        // The restful service start on this port
        int port = parseIntArgs(args, 1, DEFAULT_PORT);
        // The websocket service start on this port
        int webSocketPort = parseIntArgs(args, 2, WEBSOCKET_DEFAULT_PORT);
        startServer(port, webSocketPort);
    }

    private static void startServer(int port, int webSocketPort)
    {
        Configuration configuration = new Configuration();
        configuration.setPositionReceiverUrl("ws://localhost:"+webSocketPort+"/position");
        // The track belongs to the yard
        Track track = new Track(1, 1, configuration);

        // Handler user's operations
        OperationHandler operationHandler = new OperationHandler(track);
        YardServer server = new YardServer("localhost", port, webSocketPort);
        server.start(operationHandler);
    }

    private static int parseIntArgs(String[] args, int index, int defaultVal){
        int port = defaultVal;
        if(args.length>index){
            try {
                port = Integer.parseInt(args[index]);
            }catch (Exception e){
                System.out.println("Illegal format args, use default value");
            }

        }
        return port;
    }
}
