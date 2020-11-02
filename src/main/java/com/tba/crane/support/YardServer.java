package com.tba.crane.support;


import com.jfinal.config.*;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.ext.handler.UrlSkipHandler;
import com.jfinal.server.undertow.UndertowServer;
import com.jfinal.template.Engine;
import com.tba.crane.Main;
import io.undertow.Undertow;
import io.undertow.server.handlers.BlockingHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.resource.ClassPathResourceManager;

import static io.undertow.Handlers.path;
import static io.undertow.Handlers.resource;

/**
 * Start a Restful service to handle all user operation requests through Restful API,</br>
 * a Websocket Endpoint to receive position updated message from Crane thread,
 * and a Websocket Endpoint to send position updated message to the UI,
 *
 * @author xiaoxue zheng
 * @version 1.0
 * @date 2020-10-26
 */
public final class YardServer {

  /**
   * Start the service on this host, default is localhost
   */
  private final String host;

  /**
   * Start the Restful service on this port, default is 8081, can be passed by the first app start args
   */
  private final int port;

  /**
   * Start the Websocket server on this port, default is 8082, can be passed by the second app start args
   */
  private final int webSocketPort;

  public YardServer(String host, int port, int webSocketPort) {
    this.host = host;
    this.port = port;
    this.webSocketPort = webSocketPort;
    System.setProperty("undertow.port", webSocketPort + "");
  }

  public void start(PathHandler handler) {
    // Start the Restful service, use Undertow container, /crane is the prefix
    Undertow server = Undertow.builder()
        .addHttpListener(port, "localhost")
        .setHandler(path().addPrefixPath("/",
            resource(new ClassPathResourceManager(Main.class.getClassLoader()))
                .setAllowed((exchange)->true)
                .addWelcomeFiles("index.html"))
            .addPrefixPath("/crane", new BlockingHandler(handler)))
        .build();
    server.start();

    // Start the WebSocket server,
    UndertowServer.create(WebsocketConfig.class)
        .configWeb(builder -> {
          builder.addWebSocketEndpoint(PositionReceiver.class);
          builder.addWebSocketEndpoint(PositionDispatcher.class);
        }).start();
  }

  public static class WebsocketConfig extends JFinalConfig {

    @Override
    public void configConstant(Constants constants) {

    }

    @Override
    public void configRoute(Routes routes) {

    }

    @Override
    public void configEngine(Engine engine) {

    }

    @Override
    public void configPlugin(Plugins plugins) {

    }

    @Override
    public void configInterceptor(Interceptors interceptors) {

    }

    @Override
    public void configHandler(com.jfinal.config.Handlers handlers) {
      handlers.add(new ContextPathHandler("ctx"));
      handlers.add(new UrlSkipHandler("^/position", false));
      handlers.add(new UrlSkipHandler("^/monitor", false));
    }
  }

}
