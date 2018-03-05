package main.utils;

import main.SparkMainApp;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;


@WebSocket
public class WebSocketHelper {

    private String sender, msg;

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        user.getUpgradeRequest().getCookies().forEach(cookie -> {
            if(cookie.getName().equals("user")) SparkMainApp.loginUser(user,  cookie.getValue());
        });
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        SparkMainApp.logoutUser(user);
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        //broadcast messages here
    }


}
