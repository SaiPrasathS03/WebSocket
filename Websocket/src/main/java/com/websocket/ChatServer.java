package com.websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint("/chat")
public class ChatServer {

    // Set to hold all active WebSocket sessions (connected clients)
    private static Set<Session> clients = new HashSet<>();
    @OnOpen
    public void onOpen(Session session) {
        clients.add(session);
        System.out.println("New connection: " + session.getId());
    }

    // Called when a message is received from the client
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Message received: " + message);

        // Broadcast the received message to all connected clients
        for (Session client : clients) {
            if (client.isOpen() && client != session) { // Don't send the message back to the sender
                try {
                    client.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Called when the connection is closed
    @OnClose
    public void onClose(Session session) {
        clients.remove(session);
        System.out.println("Connection closed: " + session.getId());
    }

    // Called if there's an error in the WebSocket connection
    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }
}
