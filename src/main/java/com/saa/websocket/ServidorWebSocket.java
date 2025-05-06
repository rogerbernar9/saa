package com.saa.websocket;

import org.java_websocket.server.WebSocketServer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;

public class ServidorWebSocket extends WebSocketServer {
    private static ServidorWebSocket instancia;
    private static Thread threadServidor;
    private Exception ultimaExcecao = null;


    private volatile boolean rodando = false;

    public static ServidorWebSocket getInstance() {
        if (instancia == null) {
            instancia = new ServidorWebSocket(new InetSocketAddress(8087));
        }
        return instancia;
    }

    private ServidorWebSocket(InetSocketAddress address) {
        super(address);
    }

    public void iniciarEmThread() {
        if (threadServidor == null || !threadServidor.isAlive()) {
            threadServidor = new Thread(() -> {
                try {
                    this.start();
                    System.out.println("Servidor WebSocket iniciado na porta: " + getPort());
                } catch (Exception e) {
                    ultimaExcecao = e;
                    System.err.println("Erro ao iniciar servidor WebSocket: " + e.getMessage());
                }
            });
            threadServidor.start();
        } else {
            System.out.println("Servidor WebSocket já está em execução.");
        }
    }

//    public boolean estaRodando() {
//        return threadServidor != null && threadServidor.isAlive();
//    }

    public boolean estaRodando() {
        return rodando;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("Nova conexão: " + conn.getRemoteSocketAddress());
        conn.send("Bem-vindo ao servidor WebSocket!");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Conexão encerrada: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Mensagem recebida: " + message);
        for (WebSocket socket : getConnections()) {
            socket.send(">> " + message);
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("Erro no WebSocket: " + ex.getMessage());
    }

    @Override
    public void onStart() {
        rodando = true;
        System.out.println("Servidor WebSocket foi iniciado.");
    }

    public boolean isPortInUse(int port) {
        try (java.net.Socket socket = new java.net.Socket("localhost", port)) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Exception getUltimaExcecao() {
        return ultimaExcecao;
    }
}