package com.saa.websocket;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;
public class ClienteWebSocket extends WebSocketClient {
    public ClienteWebSocket(URI serverUri) {
        super(serverUri);
    }
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Conectado ao servidor WebSocket");
        send("Olá do cliente!");
    }
    @Override
    public void onMessage(String message) {
        System.out.println("Mensagem recebida do servidor: " + message);
    }
    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Conexão encerrada: " + reason);
    }
    @Override
    public void onError(Exception ex) {
        System.err.println("Erro no cliente WebSocket: " + ex.getMessage());
    }
    public static void main(String[] args) {
        try {
            URI uri = new URI("ws://localhost:8087");
            ClienteWebSocket cliente = new ClienteWebSocket(uri);
            cliente.connectBlocking();
            cliente.send("Mensagem de teste do painel chamador.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}