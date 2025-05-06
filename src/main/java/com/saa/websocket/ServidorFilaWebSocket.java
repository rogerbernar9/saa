package com.saa.websocket;

import org.java_websocket.WebSocket;
import org.java_websocket.server.WebSocketServer;
import org.java_websocket.handshake.ClientHandshake;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.Queue;

public class ServidorFilaWebSocket extends WebSocketServer {
    private static Queue<String> filaSenhas = new LinkedList<>();
    private boolean rodando = false;

    private static String ultimaSenhaChamada = null;
    public ServidorFilaWebSocket() {
        super(new InetSocketAddress(8087));
    }
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("Cliente conectado: " + conn.getRemoteSocketAddress());
        conn.send("Conectado ao servidor de senhas.");
    }
    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Cliente desconectado.");
    }
    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Mensagem recebida: " + message);
        switch (message) {
            case "chamar_proximo":
                if (!filaSenhas.isEmpty()) {
                    ultimaSenhaChamada = filaSenhas.poll();
                    conn.send("Chamando senha: " + ultimaSenhaChamada);
                } else {
                    conn.send("Fila vazia.");
                }
                break;
            case "repetir_chamada":
                if (ultimaSenhaChamada != null) {
                    conn.send("Repetindo senha: " + ultimaSenhaChamada);
                } else {
                    conn.send("Nenhuma senha chamada ainda.");
                }
                break;
            default:
                if (message.startsWith("nova_senha:")) {
                    String novaSenha = message.split(":")[1];
                    filaSenhas.add(novaSenha);
                    System.out.println("Nova senha adicionada: " + novaSenha);
                }
        }
    }
    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("Erro: " + ex.getMessage());
    }
    @Override
    public void onStart() {
        this.rodando = true;
        System.out.println("Servidor de senhas iniciado.");
    }

    public void iniciarEmThread() {
        new Thread(() -> {
            try {
                this.start();
            } catch (Exception e) {
                System.err.println("Erro ao iniciar WebSocket: " + e.getMessage());
            }
        }).start();
    }
    public boolean estaRodando() {
        return rodando;
    }

    public void pararServidor() {
        try {
            rodando = false;
            this.stop();
        } catch (Exception e) {
            System.err.println("Erro ao parar o servidor: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ServidorFilaWebSocket servidor = new ServidorFilaWebSocket();
        servidor.start();
    }
}
