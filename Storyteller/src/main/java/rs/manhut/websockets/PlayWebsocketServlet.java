package rs.manhut.websockets;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class PlayWebSocketServlet extends WebSocketServlet {

    @Override
    public void configure(WebSocketServletFactory webSocketServletFactory) {
        webSocketServletFactory.getPolicy().setIdleTimeout(600000);
        webSocketServletFactory.register(PlaySocket.class);
    }
}
