package com.tba.crane.operation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tba.crane.model.Track;
import com.tba.crane.operation.request.MoveCraneRequest;
import com.tba.crane.operation.request.MoveOutCraneRequest;
import com.tba.crane.operation.request.SetCraneRequest;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.PathHandler;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

/**
 *
 * The class handles all the requests from restful apis.
 * It is passed to the UndertowServer
 *
 * @author xiaoxue zheng
 * @version 1.0
 * @date 2020-10-26
 */
public class OperationHandler extends PathHandler
{
    private static final ObjectMapper mapper = new ObjectMapper();
    private final Track track;

    public OperationHandler(Track track){
        this.track = track;
    }


    @Override
    public void handleRequest(HttpServerExchange exchange) {
        OperationEnum operation = parseOperation(exchange.getRelativePath());
        InputStream inputStream = exchange.getInputStream();
        try{
            switch (operation){
                case SET:
                    track.instantiateCranes(mapper.readValue(IOUtils.toString(inputStream), SetCraneRequest.class).getCranes());
                    exchange.getResponseSender().send(OperationResponse.from("success"));
                    break;
                case MOVE:
                    MoveCraneRequest moveRequest = mapper.readValue(IOUtils.toString(inputStream), MoveCraneRequest.class);
                    track.moveCrane(moveRequest.getCraneId(), moveRequest.getNewPosition());
                    exchange.getResponseSender().send(OperationResponse.from("success"));
                    break;
                case MOVEOUT:
                    MoveOutCraneRequest moveOutRequest = mapper.readValue(IOUtils.toString(inputStream), MoveOutCraneRequest.class);
                    track.moveOutCrane(moveOutRequest.getCraneId());
                    exchange.getResponseSender().send(OperationResponse.from("success"));
                    break;
                case RELOAD:
                    String positions = track.getCranePositions();
                    exchange.getResponseSender().send(OperationResponse.from(positions));
                    break;
                default:
                    throw new OperatingCraneException("unsupported operation");
            }

        }catch (Exception e){
            exchange.setResponseCode(500);
            exchange.getResponseSender().send(OperationResponse.from(e.getMessage()));
        }
    }



    public static OperationEnum parseOperation(String path){
        try {
            //path example: /crane/set  /crane/move   /crane/moveout
            return OperationEnum.valueOf(path.substring(path.lastIndexOf("/")+1).toUpperCase());

        }catch (Exception e){
            throw new OperatingCraneException(e);
        }

    }

    public static class OperationResponse{
        private String message;

        public OperationResponse(String message){
            this.message = message;
        }

        public static String from(String message) {
            try {
                return mapper.writeValueAsString(new OperationResponse(message));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return null;
        }

        public String getMessage(){
            return this.message;
        }
    }



}
