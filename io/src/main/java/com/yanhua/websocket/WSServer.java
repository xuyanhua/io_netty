package com.yanhua.websocket;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.ServletException;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 打开浏览器：http://localhost:8080/
 * 1、websocket = new WebSocket("ws://localhost:8080/wsdemo/chat")
 * 2、websocket.onmessage = function(s){console.info(s.data);}
 * 3、websocket.send('12343434');
 *
 * @author xuyanhua
 * @description:
 * @date 2019/3/27 下午1:41
 */
@ServerEndpoint(value = "/chat")
public class WSServer {

    private Session session;
    private final static Set<WSServer> connections = new CopyOnWriteArraySet<>();

    @OnOpen
    public void start(Session session) {
        this.session = session;
        connections.add(this);
        String message = String.format("%s %s", session.getId(), "has joined.");
        broadcast(message);
    }

    @OnClose
    public void end() {
        connections.remove(this);
        String message = String.format("% %s", session.getId(), "has disconnected.");
        broadcast(message);
    }

    @OnMessage
    public void incoming(String message) {
        String msg = String.format("%s %s %s", session.getId(), "said:", message);
        broadcast(msg);
    }

    @OnError
    public void onError(Throwable t) throws Throwable {

    }

    private static void broadcast(String message) {
        for (WSServer client : connections) {
            try {
                client.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws LifecycleException, ServletException {
        new WSServer();
        Tomcat tomcat = new Tomcat();
        tomcat.setHostname("localhost");
        tomcat.setPort(8080);
        tomcat.start();
        String target = "tempwebapp";
        File targetFolder = new File(target + File.separator + "WEB-INF" + File.separator + "lib");
        targetFolder.mkdirs();
        tomcat.addWebapp("/wsdemo", new File(target).getAbsolutePath());
        tomcat.getServer().await();
    }


}
