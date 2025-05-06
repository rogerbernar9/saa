package com.saa.websocket;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;
public class ClienteWebSocketSimulado extends WebSocketClient {
    private final String nome;
    public ClienteWebSocketSimulado(URI serverUri, String nome) {
        super(serverUri);
        this.nome = nome;
    }
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println(nome + " conectado ao servidor WebSocket.");
        send(nome + " entrou na conversa.");
    }
    @Override
    public void onMessage(String message) {
        System.out.println(nome + " recebeu: " + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println(nome + " desconectado: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        System.err.println(nome + " - erro: " + ex.getMessage());
    }

    public static void main(String[] args) {
        try {
            URI uri = new URI("ws://localhost:8087");
            ClienteWebSocketSimulado cliente1 = new ClienteWebSocketSimulado(uri, "Painel 1");
            ClienteWebSocketSimulado cliente2 = new ClienteWebSocketSimulado(uri, "Painel 2");
            cliente1.connectBlocking();
            cliente2.connectBlocking();
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                int contador = 0;
                @Override
                public void run() {
                    if (contador >= 100) {
                        timer.cancel();
                        cliente1.close();
                        cliente2.close();
                        System.out.println("Fim da simulação.");
                        return;
                    }
                    String msg1 = "Painel 1: Mensagem " + contador+"/n";
                    String msg2 = "Painel 2: Resposta " + contador+"/n";
                    cliente1.send(msg1);
                    cliente2.send(msg2);
                    contador += 2; // simula envio a cada 5 segundos
                }
            }, 0, 2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
